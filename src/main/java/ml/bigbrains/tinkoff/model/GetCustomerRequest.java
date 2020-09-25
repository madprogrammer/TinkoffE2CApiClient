package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCustomerRequest extends SignedCustomerKeyRequest {
    
    @JsonCreator
    public GetCustomerRequest(
            @JsonProperty(value = "CustomerKey", required = true) String customerKey,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        super(customerKey, terminalKey);
    }

    @Override
    public String getUri() {
        return "/GetCustomer";
    }
}
