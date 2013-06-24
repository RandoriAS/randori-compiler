package randori.compiler.internal.projects;


/**
 * @author Frédéric THOMAS
 *         Date: 23/05/13
 *         Time: 15:21
 */
abstract public class RandoriTestCaseBase {

    protected static final String RESOURCES_TEST_BASE_PATH = "src/test/resources/";

    /**
     * Return relative path to the test data. Not intended to be overridden.
     *
     * @return Project relative path to the test data.
     */
    protected String getTestDataPath()
    {
        return RESOURCES_TEST_BASE_PATH + this.getClass().getPackage().getName().replace(".", "/") + "/";
    }
}
