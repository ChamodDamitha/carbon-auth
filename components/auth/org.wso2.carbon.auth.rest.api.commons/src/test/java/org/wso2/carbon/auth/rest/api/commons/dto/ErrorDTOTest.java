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

package org.wso2.carbon.auth.rest.api.commons.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for ErrorDTO
 */
public class ErrorDTOTest {
    private static final Long CODE = 100100L;
    private static final String DESCRIPTION = "error_desc";
    private static final String MESSAGE = "error_message";
    private static final List<ErrorListItemDTO> MORE_INFO = new ArrayList<ErrorListItemDTO>() {
        {
            add(new ErrorListItemDTO(123L, "error-item-1"));
            add(new ErrorListItemDTO(456L, "error-item-2"));
        }
    };

    @Test
    public void testErrorFromSetters() {
        ErrorDTO errorDTO = getSampleErrorFormSetters();
        assertSampleError(errorDTO);
    }

    @Test
    public void testErrorFromBuilderPattern() {
        ErrorDTO errorDTO = getSampleErrorFormBuilderPattern();
        assertSampleError(errorDTO);
    }

    @Test
    public void testErrorEquality() {
        ErrorDTO errorDTOFromSetters = getSampleErrorFormBuilderPattern();
        ErrorDTO errorDTOFromBuilderPattern = getSampleErrorFormBuilderPattern();
        Assert.assertEquals(errorDTOFromSetters, errorDTOFromBuilderPattern);
        Assert.assertEquals(errorDTOFromSetters.hashCode(), errorDTOFromBuilderPattern.hashCode());
    }

    private ErrorDTO getSampleErrorFormSetters() {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(CODE);
        errorDTO.setDescription(DESCRIPTION);
        errorDTO.setMessage(MESSAGE);
        errorDTO.setMoreInfo(MORE_INFO);
        return errorDTO;
    }

    private ErrorDTO getSampleErrorFormBuilderPattern() {
        return new ErrorDTO().code(CODE).description(DESCRIPTION).message(MESSAGE).moreInfo(MORE_INFO);
    }

    private void assertSampleError(ErrorDTO errorDTO) {
        Assert.assertEquals(errorDTO.getCode(), CODE);
        Assert.assertEquals(errorDTO.getDescription(), DESCRIPTION);
        Assert.assertEquals(errorDTO.getMessage(), MESSAGE);
        Assert.assertEquals(errorDTO.getMoreInfo(), MORE_INFO);

        Assert.assertTrue(errorDTO.toString().contains(CODE + ""));
        Assert.assertTrue(errorDTO.toString().contains(DESCRIPTION));
    }
}
