/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller(value = "/paramStore", produces = MediaType.APPLICATION_JSON)
public class ParamaterStoreController {

    @ReflectiveAccess
    @Inject
    private SsmClient ssmClient;

    @Post(value = "/")
    public Result createBucket(@Valid @Body Parameter parameter) {
        try {
            PutParameterRequest putParameterRequest = PutParameterRequest.builder()
                    .name(parameter.getName())
                    .type(parameter.getType())
                    .value(parameter.getValue())
                    .build();
            PutParameterResponse parameterResponse = ssmClient.putParameter(putParameterRequest);
            return new Result(parameterResponse.responseMetadata().requestId(),
                    String.valueOf(parameterResponse.sdkHttpResponse().statusCode()),
                    parameterResponse.toString());
        } catch (SsmException ssmException) {
            return new Result(ssmException.requestId(), String.valueOf(ssmException.statusCode()), ssmException.getMessage());
        } catch (SdkException sdkException) {
            return new Result("N/A", sdkException.getMessage(), sdkException.getLocalizedMessage());
        }
    }

    @Get("/{/paramPath:.*}")
    public ListParamsResult index(String paramPath) {
        try {
            GetParametersByPathResponse parametersByPath = ssmClient.getParametersByPath(GetParametersByPathRequest.builder()
                    .path("/" + paramPath)
                    .recursive(true)
                    .build());
            return new ListParamsResult(
                    parametersByPath.responseMetadata().requestId(),
                    String.valueOf(parametersByPath.sdkHttpResponse().statusCode()),
                    parametersByPath.parameters().stream()
                            .map(x -> new Parameter(x.name(), x.typeAsString(), x.value()))
                            .collect(Collectors.toList()));
        } catch (SsmException ssmException) {
            return new ListParamsResult(ssmException.requestId(), String.valueOf(ssmException.statusCode()), ssmException.getMessage());
        } catch (SdkException sdkException) {
            return new ListParamsResult("N/A", sdkException.getMessage(), sdkException.getMessage());
        }
    }
}
