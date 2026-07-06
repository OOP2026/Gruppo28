package project.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.library.GeneralCodingRules.*;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "project")
public class BestPracticesTest {

    @ArchTest
    public static final ArchRule java_util_logging_is_not_used = noClasses()
            .should(USE_JAVA_UTIL_LOGGING);

    @ArchTest
    public static final ArchRule field_injection_is_not_used = noFields()
            .should(BE_ANNOTATED_WITH_AN_INJECTION_ANNOTATION);
}