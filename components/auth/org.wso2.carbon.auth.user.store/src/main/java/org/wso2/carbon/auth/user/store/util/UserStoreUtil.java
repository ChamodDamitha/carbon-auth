/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.auth.user.store.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.auth.user.store.configuration.models.AttributeConfiguration;
import org.wso2.carbon.auth.user.store.configuration.models.UserStoreConfiguration;
import org.wso2.carbon.auth.user.store.connector.Attribute;
import org.wso2.carbon.auth.user.store.connector.UserStoreConnector;
import org.wso2.carbon.auth.user.store.connector.UserStoreConnectorFactory;
import org.wso2.carbon.auth.user.store.constant.UserStoreConstants;
import org.wso2.carbon.auth.user.store.exception.GroupNotFoundException;
import org.wso2.carbon.auth.user.store.exception.UserNotFoundException;
import org.wso2.carbon.auth.user.store.exception.UserStoreConnectorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.security.auth.callback.PasswordCallback;

/**
 * Identity Management Util.
 */
public class UserStoreUtil {

    private static Logger log = LoggerFactory.getLogger(UserStoreUtil.class);

    private UserStoreUtil() {

    }

    /**
     * Generate UUID.
     *
     * @return UUID as a string.
     */
    public static String generateUUID() {

        String random = UUID.randomUUID().toString();
        random = random.replace("/", "_");
        random = random.replace("=", "a");
        random = random.replace("+", "f");
        return random;
    }

    /**
     * Add default admin user
     *
     * @param config UserStoreConfiguration
     * @throws UserStoreConnectorException when error occurs while adding admin user
     */
    public static void addAdminUser(UserStoreConfiguration config) throws UserStoreConnectorException {
        //adding default admin user
        char[] password = config.getSuperUserPass().toCharArray();
        String user = config.getSuperUser();
        UserStoreConnector connector = UserStoreConnectorFactory.getUserStoreConnector();

        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute(UserStoreConstants.CLAIM_USERNAME, user));
        attributeList.add(new Attribute(UserStoreConstants.CLAIM_ID, UserStoreUtil.generateUUID()));
        try {
            String uid = connector.getConnectorUserId(UserStoreConstants.CLAIM_USERNAME, user);
            if (uid != null) {
                log.debug("Admin user already exists.");
                return;
            }
        } catch (UserNotFoundException e) {
            //not logging exception since, this code is to handler default user populating logic
            //when user not exist
            log.debug("Admin user not exist", e);
        }

        String userId = connector.addUser(attributeList);
        PasswordCallback passwordCallback = new PasswordCallback(UserStoreConstants.PASSWORD_URI, false);
        passwordCallback.setPassword(password);
        connector.addCredential(userId, passwordCallback);
        log.debug("Added default admin user.");
    }

    /**
     * Add default admin Group
     *
     * @param config UserStoreConfiguration
     * @throws UserStoreConnectorException when error occurs while adding admin Group
     */
    public static void addAdminGroup(UserStoreConfiguration config) throws UserStoreConnectorException {
        //adding default admin Group
        String user = config.getSuperUser();
        String group = config.getSuperUserGroup();
        UserStoreConnector connector = UserStoreConnectorFactory.getUserStoreConnector();
        try {
            String id = connector.getConnectorGroupId(UserStoreConstants.GROUP_DISPLAY_NAME, group);
            if (id != null) {
                log.debug("Admin Group already exists.");
                return;
            }
        } catch (GroupNotFoundException e) {
            log.debug("Admin Group Doesn't exist", e);
        }
        try {
            String uid = connector.getConnectorUserId(UserStoreConstants.CLAIM_USERNAME, user);
            if (uid != null) {
                List<Attribute> attributes = new ArrayList<>();
                attributes.add(new Attribute(UserStoreConstants.CLAIM_ID, UserStoreUtil.generateUUID()));
                attributes.add(new Attribute(UserStoreConstants.GROUP_DISPLAY_NAME, group));
                String groupId = connector.addGroup(attributes);
                if (groupId != null) {
                    connector.updateUsersOfGroup(groupId, Collections.singletonList(uid));
                }
            }
        } catch (UserNotFoundException e) {
            //not logging exception since, this code is to handler default user populating logic
            //when user not exist
            log.debug("Admin user not exist", e);
        }
    }

    /**
     * Add default user attributes
     *
     * @param config UserStoreConfiguration
     * @throws UserStoreConnectorException when error occurs while adding default attributes
     */
    public static void addDefaultAttributes(UserStoreConfiguration config) throws UserStoreConnectorException {

        UserStoreConnector connector = UserStoreConnectorFactory.getUserStoreConnector();
        List<AttributeConfiguration> attributeConfigurations = config.getAttributes();

        //Iterate the list of attribute in the config from last index to 0. If any config found to be exist in the DB, 
        // then stop the process and return. This is done to reduce the effect to the startup time. 
        // Any new attribute needs to be added to the last in the configuration.
        for (int i = attributeConfigurations.size() - 1; i >= 0; i--) {
            if (connector.getAttributeConfigByURI(attributeConfigurations.get(i).getAttributeUri()) == null) {
                connector.addAttribute(attributeConfigurations.get(i));
            } else {
                return;
            }
        }
    }
}
