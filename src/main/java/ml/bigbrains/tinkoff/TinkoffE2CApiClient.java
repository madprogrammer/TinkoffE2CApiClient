package ml.bigbrains.tinkoff;

import ml.bigbrains.tinkoff.model.SignedRequest;
import okhttp3.*;

import java.io.IOException;

public class TinkoffE2CApiClient {

    private static String TEST_API_BASE = "https://rest-api-test.tinkoff.ru";
    private static String PROD_API_BASE = "https://securepay.tinkoff.ru";

    private final boolean testMode;

    private final OkHttpClient httpClient;

    public TinkoffE2CApiClient(OkHttpClient httpClient, boolean testMode) {
        this.testMode = testMode;
        this.httpClient = httpClient;
    }

    public TinkoffE2CApiClient(OkHttpClient httpClient) {
        this(httpClient, false);
    }

    public String getBaseUri() {
        if (this.testMode)
            return TEST_API_BASE;
        else
            return PROD_API_BASE;
    }

    public Response sendRequest(SignedRequest request) throws IOException {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        request.toMap().forEach(bodyBuilder::add);

        Request httpRequest = new Request.Builder()
                .url(getBaseUri() + request.getUri())
                .post(bodyBuilder.build())
                .build();

        return httpClient.newCall(httpRequest).execute();
    }
}
