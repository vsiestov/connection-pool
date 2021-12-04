package vsi;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PoolConfig {
    private String url;
    private String username;
    private String password;
    @Builder.Default
    private int size = 10;
    private String driverName;
}
