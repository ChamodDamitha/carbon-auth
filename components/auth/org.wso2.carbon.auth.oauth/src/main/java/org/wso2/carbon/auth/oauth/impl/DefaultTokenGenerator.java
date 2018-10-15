/*
 *
 *   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.auth.oauth.impl;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.oauth2.sdk.token.Tokens;
import org.wso2.carbon.auth.oauth.OAuthConstants;
import org.wso2.carbon.auth.oauth.TokenGenerator;
import org.wso2.carbon.auth.oauth.dto.AccessTokenContext;

/**
 * Contains token generation logic
 */
public class DefaultTokenGenerator implements TokenGenerator {

    @Override
    public void generateAccessToken(AccessTokenContext context) {

        Scope scope = (Scope) context.getParams().get(OAuthConstants.SCOPES);
        long defaultValidityPeriod = (long) context.getParams().get(OAuthConstants.VALIDITY_PERIOD);
        Object generatedAccessToken = context.getParams().get(OAuthConstants.TOKEN);
        BearerAccessToken accessToken;
        if (generatedAccessToken != null) {
            accessToken = new BearerAccessToken((String) generatedAccessToken, defaultValidityPeriod, scope);
        } else {
            accessToken = new BearerAccessToken(defaultValidityPeriod, scope);
        }
        String grantTypeValue = (String) context.getParams().get(OAuthConstants.GRANT_TYPE);
        RefreshToken refreshToken = null;
        if (!GrantType.CLIENT_CREDENTIALS.getValue().equals(grantTypeValue)) {
            refreshToken = new RefreshToken();
        }
        Tokens tokens = new Tokens(accessToken, refreshToken);
        context.setAccessTokenResponse(new AccessTokenResponse(tokens));
        context.setSuccessful(true);
    }
}
