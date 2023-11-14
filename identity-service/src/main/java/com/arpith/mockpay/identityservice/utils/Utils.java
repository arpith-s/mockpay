package com.arpith.mockpay.identityservice.utils;

import com.arpith.mockpay.identityservice.constant.Constant;
import com.arpith.mockpay.identityservice.enumeration.UserType;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.EnumMap;

@UtilityClass
public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static boolean isValidEnum(String enumValue, Class<? extends Enum<?>> enumClass) {
        LOG.info("Entering Utils.isValidEnum");
        Enum<?>[] enumValues = enumClass.getEnumConstants();
        for (Enum<?> currentEnumValue : enumValues) {
            if (currentEnumValue.name().equals(enumValue)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("squid:S3011")
    public static boolean areAllFieldsNull(Object object) throws IllegalAccessException {
        LOG.info("Entering Utils.areAllFieldsNull");
        for (Field field : object.getClass().getDeclaredFields()) {
            // This accessibility update should be removed.
            field.setAccessible(true);
            if (field.get(object) != null)
                return false;
        }
        return true;
    }

    public String getAuthoritiesForUser(UserType userType) {
        LOG.info("Entering Utils.getAuthoritiesForUser");
        var authoritiesMap = new EnumMap<UserType, String>(UserType.class);

        var serviceAuthoritiesString = String.join(Constant.DELIMITER_COMMA, Constant.SERVICE_AUTHORITIES);
        var clientAuthoritiesString = String.join(Constant.DELIMITER_COMMA, Constant.CLIENT_AUTHORITIES);

        authoritiesMap.put(UserType.SERVICE, serviceAuthoritiesString);
        authoritiesMap.put(UserType.CLIENT, clientAuthoritiesString);

        return authoritiesMap.getOrDefault(userType, Constant.DELIMITER_EMPTY);
    }


}
