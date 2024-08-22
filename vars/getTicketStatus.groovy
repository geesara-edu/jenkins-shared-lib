def call(Map params) {
    String jiraUrl = params.jiraUrl
    String projectKey = params.projectKey
    String emailId = params.emailId
    String token = params.token
    String jira_issue_id = params.JIRA_ISSUE_ID

  def statusResponse = sh(script: "curl -X GET -u ${env.emailId}:${env.token} -H 'Content-Type: application/json' ${env.jiraUrl}/${env.jira_issue_id}", returnStdout: true)
  
  //echo "Jira API Response: ${response}"

  def approvalStatus = "Not Approved"
  def maxAttempts = 10
  def waitInterval = 10
                    

  echo "Checking ticket status"

  while (approvalStatus != "Done" && maxAttempts > 0) {
  echo "Gettins ticket status of ${env.JIRA_ISSUE_ID}"
  def statusResponse = sh(script: "curl -X GET -u ${env.EMAIL_ID}:${env.TOKEN} -H 'Content-Type: application/json' ${env.jiraUrl}/${env.JIRA_ISSUE_ID}", returnStdout: true)
    // Capture response in status.json

    echo "Jira API Response: ${statusResponse}"
    def statusJson = new groovy.json.JsonSlurper().parseText(statusResponse)
    approvalStatus = statusJson.fields.status.name

    echo "Current status: ${approvalStatus}"

    if (approvalStatus == "Done") {
        break
    }
                        

    }

    if (approvalStatus == "Done") {
        echo "Ticket approved!"
    } else {
        error "Ticket not approved within ${maxAttempts} attempts"
 }
}
