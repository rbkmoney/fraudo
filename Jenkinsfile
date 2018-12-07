#!groovy
build('fraudo', 'docker-host') {
    checkoutRepo()
    loadBuildUtils()

    def javaLibPipeline
    runStage('load JavaLib pipeline') {
        javaLibPipeline = load("build_utils/jenkins_lib/pipeJavaLib.groovy")
    }

    def buildImageTag = "4799280a02cb73761a3ba3641285aac8ec4ec482"
    javaLibPipeline(buildImageTag)
}