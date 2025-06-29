# GitHub Actions Dependency Graph Fix

## Issue Description

The GitHub Actions workflow was encountering a **403 Forbidden** error when trying to submit dependency snapshots:

```
Error: HTTP Status 403 for request POST https://api.github.com/repos/dootamroy/DEMO-SPRINGBOOT/dependency-graph/snapshots
Error: Response body: {
  "message": "Resource not accessible by integration",
  "documentation_url": "https://docs.github.com/rest/dependency-graph/dependency-submission#create-a-snapshot-of-dependencies-for-a-repository",
  "status": 403
}
```

## Root Cause

The workflow was using an outdated version of the dependency submission action and potentially had permission issues. The `advanced-security/maven-dependency-submission-action` requires proper configuration to submit dependency snapshots.

## Solution Applied

### 1. Updated Action Version

Changed from a specific commit hash to a stable version:

```yaml
# Before
uses: advanced-security/maven-dependency-submission-action@571e99aab1055c2e71a1e2309b9691de18d6b7d6

# After
uses: advanced-security/maven-dependency-submission-action@v1
```

### 2. Removed Invalid Permissions

The explicit permissions section was removed as it contained invalid permission names and the default GitHub Actions permissions are sufficient for dependency graph submission.

## Complete Fixed Workflow

```yaml
name: Java CI with Maven

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
    - name: Build with Maven
      run: mvn -B clean package

    # Optional: Uploads the full dependency graph to GitHub to improve the quality of Dependabot alerts this repository can receive
    - name: Update dependency graph
      uses: advanced-security/maven-dependency-submission-action@v1
```

## Why This Works

### Default Permissions
GitHub Actions provides default permissions that are sufficient for most operations including dependency graph submission:
- `contents: read` - Allows reading repository contents
- `metadata: read` - Allows reading repository metadata
- `pull-requests: write` - Allows commenting on pull requests

### Action Version
Using `@v1` instead of a commit hash ensures:
- Stable, tested version of the action
- Better compatibility with GitHub's API
- Automatic bug fixes and improvements

## Benefits of Dependency Graph

1. **Security Alerts**: GitHub can detect vulnerable dependencies
2. **Dependabot**: Automated dependency updates with security insights
3. **Dependency Insights**: Visual representation of dependency relationships
4. **License Compliance**: Track open source licenses in dependencies

## Alternative Solutions

If you still encounter issues, you can:

### Option 1: Remove the dependency graph step entirely
```yaml
# Simply remove this step
- name: Update dependency graph
  uses: advanced-security/maven-dependency-submission-action@v1
```

### Option 2: Make it conditional
```yaml
- name: Update dependency graph
  if: github.event_name == 'push' && github.ref == 'refs/heads/main'
  uses: advanced-security/maven-dependency-submission-action@v1
```

### Option 3: Use explicit permissions (if needed)
```yaml
permissions:
  contents: read
  security-events: write
```

## Troubleshooting

### Common Issues

1. **403 Forbidden**: Usually resolved by updating action version
2. **401 Unauthorized**: Repository doesn't have dependency graph enabled
3. **Action not found**: Invalid action version or repository
4. **Invalid workflow**: Incorrect YAML syntax or invalid permissions

### Verification Steps

1. Check repository settings:
   - Go to Settings → Security & analysis
   - Ensure "Dependency graph" is enabled

2. Verify workflow syntax:
   - Check that YAML is valid
   - Ensure no invalid permission names

3. Test the workflow:
   - Push a change to trigger the workflow
   - Check the Actions tab for successful execution

## Best Practices

1. **Use Stable Versions**: Prefer `@v1` over commit hashes for actions
2. **Minimal Configuration**: Start with default permissions
3. **Error Handling**: Consider making dependency graph optional
4. **Documentation**: Document why the action is needed

## Related Documentation

- [GitHub Actions Permissions](https://docs.github.com/en/actions/security-guides/automatic-token-authentication#permissions-for-the-github_token)
- [Dependency Graph API](https://docs.github.com/en/rest/dependency-graph)
- [Maven Dependency Submission Action](https://github.com/advanced-security/maven-dependency-submission-action)

## Security Considerations

- The dependency submission action only submits dependency data
- It cannot read or modify repository contents beyond dependency information
- Default permissions are safe and follow the principle of least privilege 