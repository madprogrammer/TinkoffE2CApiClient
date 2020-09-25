package ml.bigbrains.tinkoff;

import okhttp3.OkHttpClient;

public interface HttpClientBuilder {
    OkHttpClient build();
}
