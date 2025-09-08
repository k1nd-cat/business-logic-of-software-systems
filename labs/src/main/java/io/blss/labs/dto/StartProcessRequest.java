package io.blss.labs.dto;

import io.blss.labs.utils.VariableValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartProcessRequest {
    private String businessKey;
    private Map<String, VariableValue> variables;
}
