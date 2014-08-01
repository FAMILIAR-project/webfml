YUI.add("aui-scheduler-event-recorder",function(e,t){var n=e.Lang,r=n.isObject,i=n.isString,s=n.isUndefined,o=e.IO.prototype._serialize,u=function(t){return t instanceof e.NodeList},a=e.DataType.DateMath,f=",",l="-",c=".",h=" ",p="scheduler-event",d="scheduler-event-recorder",v="activeView",m="allDay",g="bodyTemplate",y="boundingBox",b="cancel",w="click",E="clickoutside",S="content",x="contentBox",T="dateFormat",N="delete",C="endDate",k="event",L="eventChange",A="headerTemplate",O="isoTime",M="node",_="popover",D="recorder",P="rendered",H="save",B="scheduler",j="schedulerChange",F="startDate",I="strings",q="submit",R="top",U="visibleChange",z=e.getClassName,W=z(p),X=z(p,D),V=z(p,D,S),$=z(p,D,_),J='<form class="scheduler-event-recorder-form" id="schedulerEventRecorderForm"></form>',K='<input type="hidden" name="startDate" value="{startDate}" /><input type="hidden" name="endDate" value="{endDate}" /><label class="scheduler-event-recorder-date">{date}</label>',Q='<input class="'+V+'" name="content" value="{content}" />',G=e.Component.create({NAME:d,ATTRS:{allDay:{value:!1},content:{},duration:{value:60},dateFormat:{validator:i,value:"%a, %B %d"},event:{},eventClass:{valueFn:function(){return e.SchedulerEvent}},popover:{setter:"_setPopover",validator:r,value:{}},strings:{value:{},setter:function(t){return e.merge({"delete":"Delete","description-hint":"e.g., Dinner at Brian's",cancel:"Cancel",description:"Description",edit:"Edit",save:"Save",when:"When"},t||{})},validator:r},bodyTemplate:{value:K},headerTemplate:{value:Q}},EXTENDS:e.SchedulerEvent,prototype:{initializer:function(){var t=this;t.get(M).addClass(X),t.publish("cancel",{defaultFn:t._defCancelEventFn}),t.publish("delete",{defaultFn:t._defDeleteEventFn}),t.publish("edit",{defaultFn:t._defEditEventFn}),t.publish("save",{defaultFn:t._defSaveEventFn}),t.after(L,t._afterEventChange),t.after(j,t._afterSchedulerChange),t.popover=new e.Popover(t.get(_)),t.popover.after(U,e.bind(t._afterPopoverVisibleChange,t))},_afterEventChange:function(){var e=this;e.populateForm()},_afterPopoverVisibleChange:function(e){var t=this;if(e.newVal){t.populateForm();if(!t.get(k)){var n=t.getContentNode();n&&setTimeout(function(){n.selectText()},0)}}else t.set(k,null,{silent:!0}),t.get(M).remove()},_afterSchedulerChange:function(t){var n=this,r=t.newVal,i=r.get(y);i.delegate(w,e.bind(n._onClickSchedulerEvent,n),c+W)},_defCancelEventFn:function(){var e=this;e.get(M).remove(),e.hidePopover()},_defDeleteEventFn:function(){var e=this,t=e.get(B);t.removeEvents(e.get(k)),e.hidePopover(),t.syncEventsUI()},_defEditEventFn:function(){var e=this,t=e.get(B);e.hidePopover(),t.syncEventsUI()},_defSaveEventFn:function(e){var t=this,n=t.get(B);n.addEvents(e.newSchedulerEvent),t.hidePopover(),n.syncEventsUI()},_getFooterToolbar:function(){var t=this,n=t.get(k),r=t.get(I),i=[{label:r[H],on:{click:e.bind(t._handleSaveEvent,t)}},{label:r[b],on:{click:e.bind(t._handleCancelEvent,t)}}];return n&&i.push({label:r[N],on:{click:e.bind(t._handleDeleteEvent,t)}}),[i]},_handleCancelEvent:function(e){var t=this;t.fire("cancel"),e.domEvent&&e.domEvent.preventDefault(),e.preventDefault()},_handleClickOutSide:function(){var e=this;e.fire("cancel")},_handleDeleteEvent:function(e){var t=this;t.fire("delete",{schedulerEvent:t.get(k)}),e.domEvent&&e.domEvent.preventDefault(),e.preventDefault()},_handleEscapeEvent:function(t){var n=this;n.popover.get(P)&&t.keyCode===e.Event.KeyMap.ESC&&(n.fire("cancel"),t.preventDefault())},_handleSaveEvent:function(e){var t=this,n=t.get(k)?"edit":"save";t.fire(n,{newSchedulerEvent:t.getUpdatedSchedulerEvent()}),e.domEvent&&e.domEvent.preventDefault(),e.preventDefault()},_onClickSchedulerEvent:function(e){var t=this,n=e.currentTarget.getData(p);n&&(t.set(k,n,{silent:!0}),t.showPopover(e.currentTarget),t.get(M).remove())},_onSubmitForm:function(e){var t=this;t._handleSaveEvent(e)},_renderPopover:function(){var t=this,n=t.get(B),r=n.get(y);t.popover.render(r),t.formNode=e.Node.create(J),t.formNode.on(q,e.bind(t._onSubmitForm,t)),t.popover.get(y).addClass($),t.popover.get(x).wrap(t.formNode),r.on(E,e.bind(t._handleClickOutSide,t))},_setPopover:function(t){var n=this;return e.merge({align:{points:[e.WidgetPositionAlign.BC,e.WidgetPositionAlign.TC]},bodyContent:K,constrain:!0,headerContent:Q,preventOverlap:!0,position:R,toolbars:{footer:n._getFooterToolbar()},visible:!1,zIndex:500},t)},getContentNode:function(){var e=this,t=e.popover.get(y);return t.one(c+V)},getFormattedDate:function(){var e=this,t=e.get(k)||e,n=t.get(C),r=t.get(F),i=t._formatDate(r,e.get(T));if(t.get(m))return i;i=i.concat(f);var s=t.get(B),o=s.get(v).get(O)?a.toIsoTimeString:a.toUsTimeString;return[i,o(r),l,o(n)].join(h)},getTemplateData:function(){var e=this,t=e.get(I),n=e.get(k)||e,r=n.get(S);return s(r)&&(r=t["description-hint"]),{content:r,date:e.getFormattedDate(),endDate:n.get(C).getTime(),startDate:n.get(F).getTime()}},getUpdatedSchedulerEvent:function(t){var n=this,r=n.get(k),i={silent:!r},s=n.serializeForm();return r||(r=n.clone()),r.set(B,n.get(B),{silent:!0}),r.setAttrs(e.merge(s,t),i),r},hidePopover:function(){var e=this;e.popover.hide()},populateForm:function(){var t=this,n=t.get(g),r=t.get(A),i=t.getTemplateData();t.popover.setStdModContent("body",e.Lang.sub(n,i)),t.popover.setStdModContent("header",e.Lang.sub(r,i)),t.popover.addToolbar(t._getFooterToolbar(),"footer")},serializeForm:function(){var t=this;return e.QueryString.parse(o(t.formNode.getDOM()))},showPopover:function(e){var t=this,n=t.get(k);t.popover.get(P)||t._renderPopover(),e||(n?e=n.get(M):e=t.get(M)),u(e)&&(e=e.item(0)),t.popover.set("align.node",e),t.popover.show()}}});e.SchedulerEventRecorder=G},"2.0.0",{requires:["querystring","io-form","overlay","aui-scheduler-base","aui-popover"],skinnable:!0});