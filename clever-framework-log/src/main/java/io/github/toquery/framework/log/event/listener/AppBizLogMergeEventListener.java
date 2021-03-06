package io.github.toquery.framework.log.event.listener;

import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.log.auditor.AppBizLogAnnotationHandler;
import io.github.toquery.framework.log.entity.SysLog;
import io.github.toquery.framework.log.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Scope("singleton")
public class AppBizLogMergeEventListener implements MergeEventListener {

    @Resource
    private ISysLogService sysLogService;

    @Resource
    private AppBizLogAnnotationHandler appBizLogAnnotationHandler;

    @Override
    public void onMerge(MergeEvent event) throws HibernateException {
        Object entity = event.getEntity();
        entity = entity == null ? event.getOriginal() : entity;
        if (!(entity instanceof AppBaseEntity)) {
            log.warn("处理对象 {} 解析失败，将不记录审查日志。", entity.getClass().getSimpleName());

        }
        log.debug("接收到修改 {} 的数据操作，将记录日志。", entity.getClass().getSimpleName());
        AppBaseEntity appBaseEntity = (AppBaseEntity) entity;
        AppLogEntity appLogEntity = appBizLogAnnotationHandler.handleEntityAnnotation(appBaseEntity);
        if (appLogEntity == null) {
            return;
        }
        Map<String, Object> targetData = appBizLogAnnotationHandler.handleTargetData(appBaseEntity, appBizLogAnnotationHandler.handleEntityFields(appBaseEntity, appLogEntity));
        SysLog sysLog = appBizLogAnnotationHandler.fill2SysLog(AppLogType.MODIFY, null, targetData, appLogEntity.modelName(), appLogEntity.bizName());
        sysLogService.save(sysLog);
        log.debug("接收到新增 {} 的数据操作，记录日志完成。", entity.getClass().getSimpleName());
    }

    @Override
    public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {
        log.debug("接收到 copiedAlready 数据。\n {}", JacksonUtils.object2String(copiedAlready));
        this.onMerge(event);
    }
}
