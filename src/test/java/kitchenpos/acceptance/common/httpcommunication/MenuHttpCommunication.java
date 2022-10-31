package kitchenpos.acceptance.common.httpcommunication;

import java.util.Map;

public class MenuHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/v2/menus", requestBody)
                .build();
    }

    public static HttpCommunication getMenus() {
        return HttpCommunication.request()
                .get("/api/v2/menus")
                .build();
    }
}
