tasks.register("buildSite", BuildSiteTask::class)

task("build") {
    dependsOn("buildSite")
}
