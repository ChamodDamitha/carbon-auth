/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.auth.scope.registration.impl;

import org.wso2.carbon.auth.scope.registration.dao.ScopeDAO;
import org.wso2.carbon.auth.scope.registration.dto.Scope;
import org.wso2.carbon.auth.scope.registration.exceptions.ScopeDAOException;

import java.util.List;

/**
 * Scope Manager class which handle all scope management tasks
 */
public class ScopeManagerImpl implements ScopeManager {
    private ScopeDAO scopeDAO;

    public ScopeManagerImpl(ScopeDAO scopeDAO) {
        this.scopeDAO = scopeDAO;
    }

    /**
     * Register a scope with the bindings
     *
     * @param scope details of the scope to be registered
     * @throws ScopeDAOException Whenever register scope failed at DAO operation
     */
    @Override
    public Scope registerScope(Scope scope) throws ScopeDAOException {
        scopeDAO.addScope(scope);
        return scopeDAO.getScopeByName(scope.getName());
    }

    /**
     * Retrieve the available scope list
     *
     * @param offset Start Index of the result set to enforce pagination
     * @param limit      Number of elements in the result set to enforce pagination
     * @return Scope list
     * @throws ScopeDAOException Whenever get scopes failed at DAO operation
     */
    @Override
    public List<Scope> getScopes(Integer offset, Integer limit)
            throws ScopeDAOException {
        return scopeDAO.getScopesWithPagination(offset, limit);
    }

    /**
     * @param name Name of the scope which need to get retrieved
     * @return Retrieved Scope
     * @throws ScopeDAOException Whenever get scope failed at DAO operation
     */
    @Override
    public Scope getScope(String name) throws ScopeDAOException {
        return scopeDAO.getScopeByName(name);
    }

    /**
     * Check the existence of a scope
     *
     * @param name Name of the scope
     * @return true if scope with the given scope name exists
     * @throws ScopeDAOException Whenever check scope failed at DAO operation
     */
    @Override
    public boolean isScopeExists(String name) throws ScopeDAOException {
        return scopeDAO.isScopeExists(name);
    }

    /**
     * Delete the scope for the given scope ID
     *
     * @param name Scope ID of the scope which need to get deleted
     * @throws ScopeDAOException Whenever delete scope failed at DAO operation
     */
    @Override
    public void deleteScope(String name) throws ScopeDAOException {
        scopeDAO.deleteScopeByName(name);
    }

    /**
     * Update the scope of the given scope ID
     *
     * @param updatedScope details of updated scope
     * @return updated scope
     * @throws ScopeDAOException Whenever update scope failed at DAO operation
     */
    @Override
    public Scope updateScope(Scope updatedScope) throws ScopeDAOException {
        scopeDAO.updateScopeByName(updatedScope);
        return scopeDAO.getScopeByName(updatedScope.getName());
    }
}
