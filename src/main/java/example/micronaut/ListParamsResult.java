package example.micronaut;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Introspected
public class ListParamsResult extends Result{

    private List<Parameter> parameters;

    public ListParamsResult(@NotNull @NotBlank String requestId, @NotNull @NotBlank String status, String message) {
        this(requestId, status, null, message);

    }
    public ListParamsResult(@NotNull @NotBlank String requestId, @NotNull @NotBlank String status, List<Parameter> parameters) {
        this(requestId, status, parameters, null);
    }
    public ListParamsResult(@NotNull @NotBlank String requestId, @NotNull @NotBlank String status, List<Parameter> parameters, String message) {
        super(requestId, status, message);
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
