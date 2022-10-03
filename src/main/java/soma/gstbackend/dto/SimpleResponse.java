package soma.gstbackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class SimpleResponse {
    Map<String, Object> resopnse = new HashMap<>();
    public SimpleResponse(String key, Object value) {
        put(key, value);
    }

    public void put(String key, Object value) {
        resopnse.put(key, value);
    }
}
