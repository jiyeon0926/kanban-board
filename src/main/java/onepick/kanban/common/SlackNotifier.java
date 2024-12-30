package onepick.kanban.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SlackNotifier {

    @Value("${slack.url}")
    private String url;

    private RestTemplate restTemplate;

    public SlackNotifier(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(String message) {
        String payload = "{\"channel\": \"#one-pick\", \"username\": \"은영bot\", \"text\": \""
                + message + "\", \"icon_emoji\": \":fairy:\"}";
        restTemplate.postForEntity(url, payload, String.class);
    }
}
