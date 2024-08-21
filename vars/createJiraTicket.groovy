def call(Map params) {
    String jiraUrl = params.jiraUrl
    String projectKey = params.projectKey
    String emailId = params.emailId
    String token = params.token
  def body = """
  {
      "fields": {
          "project": {
              "key": "${projectKey}"
          },
          "summary": "Test issue from curl new ${env.BUILD_ID}",
          "description": "UAT request",
          "issuetype": {
              "name": "Task"
          }
      }
  }
  """
  def response = sh(script: "curl -u ${emailId}:${token} -X POST --data '${body}' -H 'Content-Type: application/json' ${jiraUrl}", returnStdout: true)
  echo "Jira API Response: ${response}"

  // Handle potential errors in response (optional)

  def success = response.contains('"self"')  // Check for successful response
  if (success) {
      echo 'Created Jira ticket. Parsing response for ID...'
      def responseJson = new groovy.json.JsonSlurper().parseText(response)
      def issueId = responseJson.id
      env.JIRA_ISSUE_ID = issueId
      echo "Created Jira ticket: ${issueId}"
  } else {
      error 'Failed to create Jira ticket. Check response for details.'
  }
}
