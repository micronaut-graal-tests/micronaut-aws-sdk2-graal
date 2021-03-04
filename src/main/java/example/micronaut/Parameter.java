package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;

@Introspected
public class Parameter {

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @NotBlank
    private String type;

    @NonNull
    @NotBlank
    private String value;

    public Parameter(@NonNull @NotBlank String name, @NonNull @NotBlank String type, @NonNull @NotBlank String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }
}
