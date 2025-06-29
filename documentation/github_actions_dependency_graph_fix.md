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

The workflow lacked the necessary permissions to access GitHub's dependency graph API. The `advanced-security/maven-dependency-submission-action` requires explicit permissions to submit dependency snapshots.

## Solution Applied

### 1. Added Required Permissions

Added the following permissions section to the workflow:

```yaml
# Add permissions for dependency graph access
permissions:
  contents: read
  dependency-graph: write
```

### 2. Updated Action Version

Changed from a specific commit hash to a stable version:

```yaml
# Before
uses: advanced-security/maven-dependency-submission-action@571e99aab1055c2e71a1e2309b9691de18d6b7d6

# After
uses: advanced-security/maven-dependency-submission-action@v1
```

## Complete Fixed Workflow

```yaml
name: Java CI with Maven

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

# Add permissions for dependency graph access
permissions:
  contents: read
  dependency-graph: write

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

## Permission Details

### `contents: read`
- Allows the workflow to read repository contents
- Required for accessing source code and build files

### `dependency-graph: write`
- Allows the workflow to submit dependency snapshots
- Required for the dependency submission action to work
- Enables Dependabot security alerts and dependency insights

## Benefits of Dependency Graph

1. **Security Alerts**: GitHub can detect vulnerable dependencies
2. **Dependabot**: Automated dependency updates with security insights
3. **Dependency Insights**: Visual representation of dependency relationships
4. **License Compliance**: Track open source licenses in dependencies

## Alternative Solutions

If you don't need dependency graph functionality, you can:

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

## Troubleshooting

### Common Issues

1. **403 Forbidden**: Missing `dependency-graph: write` permission
2. **401 Unauthorized**: Repository doesn't have dependency graph enabled
3. **Action not found**: Invalid action version or repository

### Verification Steps

1. Check repository settings:
   - Go to Settings → Security & analysis
   - Ensure "Dependency graph" is enabled

2. Verify workflow permissions:
   - Check that `permissions` section is present
   - Ensure `dependency-graph: write` is included

3. Test the workflow:
   - Push a change to trigger the workflow
   - Check the Actions tab for successful execution

## Best Practices

1. **Use Stable Versions**: Prefer `@v1` over commit hashes for actions
2. **Minimal Permissions**: Only grant necessary permissions
3. **Error Handling**: Consider making dependency graph optional
4. **Documentation**: Document why permissions are needed

## Related Documentation

- [GitHub Actions Permissions](https://docs.github.com/en/actions/security-guides/automatic-token-authentication#permissions-for-the-github_token)
- [Dependency Graph API](https://docs.github.com/en/rest/dependency-graph)
- [Maven Dependency Submission Action](https://github.com/advanced-security/maven-dependency-submission-action)

## Security Considerations

- The `dependency-graph: write` permission only allows submitting dependency data
- It cannot read or modify repository contents beyond dependency information
- This is a safe permission to grant for CI/CD workflows 