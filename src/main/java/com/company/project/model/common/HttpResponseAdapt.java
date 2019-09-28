package com.company.project.model.common;

import com.company.project.utils.DesensitizeUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

import lombok.Data;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
@Data
public class HttpResponseAdapt {
    private int httpCode;
    private byte[] body;
    private String context;
    private String redirectUrl;

    public String toSimplenessString(Set<String> desensitizeJsonField) {
        String result = httpCode + "," + StringUtils.trimToEmpty(redirectUrl) + ",";
        if (StringUtils.isNotEmpty(context) && !context.startsWith("<")) {
            result += CollectionUtils.isNotEmpty(desensitizeJsonField) ? DesensitizeUtil.desensitizeJson(context, desensitizeJsonField) : context;
        }
        return result;
    }
}
