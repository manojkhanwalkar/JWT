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

// For creating JSON schema from a json file , use https://www.liquid-technologies.com/online-json-to-schema-converter
// Then for creating a java object model from that schema , use the jsonschema2pojo library.

public class PojoGenerator {

    public static void main(String[] args) throws Exception {

       // generateClasses();

       testGeneratedCode();

    }

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

   public static void testGeneratedCode() throws Exception {

        URL source = PojoGenerator.class.getResource("metrics-data.json");

        String metrics = Resources.toString(source, Charsets.US_ASCII);

        var metricObj = (MetricsSchema) JSONUtil.fromJSON(metrics, MetricsSchema.class);

        var str = JSONUtil.toPrettyJSON(metricObj);

       System.out.println(str);


    }
}
