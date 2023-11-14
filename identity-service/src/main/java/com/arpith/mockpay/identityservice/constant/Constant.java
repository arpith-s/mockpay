package com.arpith.mockpay.identityservice.constant;

import com.arpith.mockpay.identityservice.enumeration.Authorities;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constant {
    public final String DELIMITER_SPACE = " ";

    public final String DELIMITER_COMMA = ",";

    public final String DELIMITER_EMPTY = "";

    public final String DELIMITER_COLON = ":";

    public final List<String> SERVICE_AUTHORITIES = List.of(
            Authorities.VERIFY_TOKEN.name(),
            Authorities.GET_USER.name()
    );

    public final List<String> CLIENT_AUTHORITIES = List.of(
            Authorities.CREATE_USER.name(),
            Authorities.GET_USER.name(),
            Authorities.UPDATE_USER.name(),
            Authorities.DELETE_USER.name(),
            Authorities.REQUEST_TOKEN.name(),
            Authorities.TRANSACT.name(),
            Authorities.GET_TRANSACTIONS.name()
    );

    // BCrypt-Encoded strings starts with "$2a$"
    public final String BCRYPT_ENCODING_FORMAT = "^\\$2a\\$\\d{2}\\$.+";

    public static final String APP_USER_CACHE_KEY_PREFIX = "appUsr::";

    public final String CONSUMER_GROUP_ID = "identity_service_group";

}
