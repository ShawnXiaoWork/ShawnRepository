package com.shawn.gpdaccounting;

import java.util.Map;

public interface DialogCallBack {
/**
 * 弹出框确认事件回调
 */
public abstract void DialogOk(int index,Map<String,Object> map);
}
