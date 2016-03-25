package gov.nist.hit.core.api.config;

import io.github.robwin.markup.builder.MarkupLanguage;
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler;

public class ApiDocsBuilder {

  public ApiDocsBuilder() {
    build();
  }

  private void build() {
    Swagger2MarkupResultHandler.outputDirectory("src/main/webapp/apidocs")
        .withMarkupLanguage(MarkupLanguage.MARKDOWN).build();
  }


}
