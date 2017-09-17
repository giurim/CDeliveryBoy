package info.solidsoft.gradle.cdeliveryboy

import nebula.test.IntegrationTestKitSpec
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

//TODO: Is it worth using nebula-test?
class PropertyOverriderFuncSpec extends IntegrationTestKitSpec {

    def "initialize and provide 2 main tasks"() {
        given:
            buildFile << """
                plugins {
                    id 'info.solidsoft.cdeliveryboy'
                }
                apply plugin: 'java'
            """.stripIndent()
        and:
            writeHelloWorld('gradle.cdeliveryboy.test.hello')
        when:
            BuildResult result = runTasks('tasks')
        then:
            result.output.contains("prepareForCiBuild - Prepares for CI build with optional release")
            result.output.contains("ciBuild - Performs CI build with optional release")
    }

    def "initialize and fail on missing Travis configuration environment"() {
        given:
            buildFile << """
                plugins {
                    id 'info.solidsoft.cdeliveryboy'
                }
                apply plugin: 'java'
            """.stripIndent()
        when:
            BuildResult result = runTasksAndFail('prepareForCiBuild')
        then:
            result.output.contains("Missing CI variables: 4 - [TRAVIS_PULL_REQUEST, TRAVIS_BRANCH, TRAVIS_COMMIT_MSG, TRAVIS_REPO_SLUG]")
    }

    //TODO: Write regression test with different Gradle versions

    private BuildResult runTasksAndFail(String... tasks) {
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(*tasks.plus("-i"))
                .withDebug(debug)
                .withPluginClasspath()
                .forwardOutput()
                .buildAndFail()
        return checkForDeprecations(result)
    }
}