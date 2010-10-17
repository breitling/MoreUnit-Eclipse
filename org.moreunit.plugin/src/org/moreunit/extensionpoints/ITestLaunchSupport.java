package org.moreunit.extensionpoints;

import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.moreunit.preferences.PreferenceConstants;

/**
 * This interface should be implemented to provide additional support for
 * launching tests of a TestType that is supported by MoreUnit (JUnit3, JUnit4
 * and TestNG at the time of this writing).
 */
public interface ITestLaunchSupport
{

    public enum TestType
    {
        JUNIT_3(PreferenceConstants.TEST_TYPE_VALUE_JUNIT_3), JUNIT_4(PreferenceConstants.TEST_TYPE_VALUE_JUNIT_4), TESTNG(PreferenceConstants.TEST_TYPE_VALUE_TESTNG);

        private final String preferenceConstant;

        private TestType(String type)
        {
            this.preferenceConstant = type;
        }

        public static TestType fromPreferenceConstant(String type)
        {
            for (int i = 0; i < values().length; i++)
            {
                TestType testType = values()[i];
                if(testType.preferenceConstant.equals(type))
                {
                    return testType;
                }
            }
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public enum Cardinality
    {
        ONE, SEVERAL;

        public static Cardinality fromElementCount(int count)
        {
            if(count <= 0)
            {
                throw new IllegalArgumentException("Unsupported cardinality: " + count);
            }
            return count == 1 ? ONE : SEVERAL;
        }
    }

    /**
     * Returns whether this instance supports launching test element(s) for the
     * given test framework, being of the given {@link IJavaElement}'s sub-type
     * and with the given cardinality.
     * 
     * @param testType the test framework (JUnit3, JUnit4, TestNG...) used by
     *            the test element(s)
     * @param elementType the {@link IJavaElement} type or sub-type of the test
     *            elements to be launched
     * @param cardinality the number of test elements to be launched:
     *            <code>ONE</code> or <code>SEVERAL</code>
     * @return <code>true</code> if the given configuration is supported,
     *         <code>false</code> otherwise
     */
    boolean isLaunchSupported(TestType testType, Class< ? extends IJavaElement> elementType, Cardinality cardinality);

    /**
     * Returns the {@link ILaunchShortcut} provided by this instance.
     */
    ILaunchShortcut getShortcut();
}
