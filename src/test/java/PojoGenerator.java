/*
 * PojoGenerator
 *
 * Dev utility for the metrics.data model.
 *   generateClasses()   regenerates the Java POJOs from metrics-schema.json with jsonschema2pojo
 *   testGeneratedCode() parses the sample metrics-data.json into those POJOs and pretty-prints it
 *
 * To make a JSON schema from a sample JSON file, use
 * https://www.liquid-technologies.com/online-json-to-schema-converter
 */
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.sun.codemodel.JCodeModel;

import metrics.data.HealthcheckSchema;
import metrics.data.MetricsSchema;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;
import util.JSONUtil;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class PojoGenerator {

    /** Runs the parse check. Uncomment generateClasses() to regenerate the model. */
    public static void main(String[] args) throws Exception {

       // generateClasses();

       testGeneratedCode();

    }

    /** Generates metrics.data classes from metrics-schema.json into a temporary directory. */
    public static void generateClasses() throws Exception {

        JCodeModel codeModel = new JCodeModel();

        URL source = PojoGenerator.class.getResource("metrics-schema.json");

        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() { // set config option by overriding method
                return true;
            }
        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(codeModel, "ClassName", "metrics.data", source);

        var codePath= Files.createTempDirectory("metrics").toFile();
        codeModel.build(codePath);

        System.out.println();



    }

    /** Parses the sample metrics-data.json into MetricsSchema and prints it again as JSON. */
    public static void testGeneratedCode() throws Exception {

        URL source = PojoGenerator.class.getResource("metrics-data.json");

        String metrics = Resources.toString(source, Charsets.US_ASCII);

        var metricObj = JSONUtil.fromJSON(metrics, MetricsSchema.class);

        var str = JSONUtil.toPrettyJSON(metricObj);

       System.out.println(str);


    }
}
