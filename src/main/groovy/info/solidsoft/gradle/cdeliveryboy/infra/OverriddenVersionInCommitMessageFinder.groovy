package info.solidsoft.gradle.cdeliveryboy.infra

import groovy.transform.CompileStatic
import info.solidsoft.gradle.cdeliveryboy.logic.OverriddenVersion

import java.util.regex.Matcher
import java.util.regex.Pattern

import static OverriddenVersion.overriddenVersionWithValue
import static OverriddenVersion.noVersionOverridden

@CompileStatic
class OverriddenVersionInCommitMessageFinder {

    //https://github.com/mojombo/semver/issues/232
    private static final Pattern SEM_VER_PATTERN = Pattern.compile("\\[#((0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)" +
            "(-(0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(\\.(0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*)?(\\+[0-9a-zA-Z-]+(\\.[0-9a-zA-Z-]+)*)?)]")
    private static final Pattern INCREMENTER_PATTERN = Pattern.compile("\\[#(MAJOR|MINOR|PATCH|PRERELEASE)]")

    private static final List<Pattern> SUPPORTED_PATTERNS = [SEM_VER_PATTERN, INCREMENTER_PATTERN]

    OverriddenVersion findOverriddenVersionInCommitMessageIfProvided(String commitMessage) {

        for (Pattern pattern : SUPPORTED_PATTERNS) {
            Matcher matcher = pattern.matcher(commitMessage)
            if (matcher.find()) {
                return overriddenVersionWithValue(matcher.group(1))
            }
        }
        return noVersionOverridden()
    }
}
