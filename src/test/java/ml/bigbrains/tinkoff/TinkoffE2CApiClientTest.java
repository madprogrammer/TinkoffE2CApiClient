package ml.bigbrains.tinkoff;

import lombok.extern.slf4j.Slf4j;
import ml.bigbrains.tinkoff.model.AddCustomerRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.CryptoPro.JCP.JCP;

import java.security.KeyStore;
import java.security.Signature;
import java.util.Base64;


@RunWith(JUnit4.class)
@Slf4j
public class TinkoffE2CApiClientTest {
    @Test
    public void signedRequest()
    {
        try {
            AddCustomerRequest addCustomerRequest = new AddCustomerRequest("321321", "123123");
            addCustomerRequest.setEmail("info@example.com");
            addCustomerRequest.setPhone("+79001000110");
            addCustomerRequest.setIp("127.0.0.1");
            addCustomerRequest.sign("HDImageStore", "testKey", null,"0080133cd1");

            log.debug(String.valueOf(addCustomerRequest.toMap()));

            byte [] signature = Base64.getDecoder().decode(addCustomerRequest.getSignatureValue());
            byte [] data = Base64.getDecoder().decode(addCustomerRequest.getDigestValue());
            KeyStore store = KeyStore.getInstance("HDImageStore");
            store.load(null, null);
            Signature instance = Signature.getInstance(JCP.GOST_SIGN_2012_512_NAME, JCP.PROVIDER_NAME);
            instance.initVerify(store.getCertificate("testKey"));
            instance.update(data);
            Assert.assertTrue(instance.verify(signature));
        } catch(CryptographicException exc) {
            log.error("Signature generation error", exc);
            Assert.fail();
        } catch (Exception exc) {
            log.error("Signature verification error", exc);
            Assert.fail();
        }
    }
}
