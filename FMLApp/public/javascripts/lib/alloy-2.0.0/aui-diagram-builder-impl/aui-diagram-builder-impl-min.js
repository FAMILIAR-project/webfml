YUI.add("aui-diagram-builder-impl",function(e,t){var n=e.Lang,r=n.isArray,i=n.isBoolean,s=n.isObject,o=n.isString,u=e.WidgetStdMod,a=e.Array,f="activeElement",l="attributeName",c="availableField",h="availableFields",p="backspace",d="boolean",v="boundary",m="boundingBox",g="builder",y="canvas",b="click",w="condition",E="connector",S="connectors",x="content",T="controls",N="controlsToolbar",C="createDocumentFragment",k="data",L="data-nodeId",A="delete",O="deleteConnectorsMessage",M="deleteNodesMessage",_="description",D="diagram",P="diagram-builder",H="diagramNode",B="diagram-node-manager",j="diagram-node",F="dragNode",I="dragging",q="editing",R="end",U="esc",z="field",W="fields",X="fieldsDragConfig",V="fork",$="graphic",J="height",K="highlightBoundaryStroke",Q="highlightDropZones",G="highlighted",Y="id",Z="join",et="keydown",tt="label",nt="lock",rt="mousedown",it="mouseenter",st="mouseleave",t="name",ot="node",ut="p1",at="p2",ft="parentNode",lt="radius",ct="rendered",ht="required",pt="selected",dt="shape",vt="shapeBoundary",mt="shapeInvite",gt="showSuggestConnector",yt="source",bt="start",wt="state",Et="stroke",St="suggest",xt="suggestConnectorOverlay",Tt="target",Nt="task",Ct="transition",kt="transitions",Lt="type",At="value",Ot="visible",Mt="width",_t="xy",Dt="zIndex",Pt="-",Ht=".",Bt="",jt="_",Ft=e.getClassName,It=Ft(D,g,T),qt=Ft(D,g,z),Rt=Ft(D,ot),Ut=Ft(D,ot,x),zt=Ft(D,ot,q),Wt=Ft(D,ot,tt),Xt=Ft(D,ot,pt),Vt=Ft(D,ot,dt,v),$t=Ft(D,ot,St,E),Jt=function(e,t){var n=r(e)?e:e.get(m).getXY();return[n[0]+t[0],n[1]+t[1]]},Kt=function(e,t){var n=t[0]-e[0],r=t[1]-e[1];return Math.sqrt(n*n+r*r)},Qt=function(e,t){var n=e.hotPoints,r=t.hotPoints,i=e.get(m).getXY(),s=t.get(m).getXY(),o,u,a,f,l=Infinity,c=[[0,0],[0,0]];for(a=0,o=n.length;a<o;a++){var h=n[a],p=Jt(i,h);for(f=0,u=r.length;f<u;f++){var d=r[f],v=Jt(s,d),g=Kt(v,p);g<l&&(c[0]=h,c[1]=d,l=g)}}return c},Gt=function(e,t){var n=r(t)?t:t.getXY(),i=r(e)?e:e.getXY();return a.map(i,function(e,t){return Math.max(0,e-n[t])})},Yt=function(t){return e.instanceOf(t,e.Connector)},Zt=function(t){return e.instanceOf(t,e.Map)},en=function(t){return e.instanceOf(t,e.DiagramBuilderBase)},tn=function(t){return e.instanceOf(t,e.DiagramNode)},nn=e.Component.create({NAME:P,ATTRS:{connector:{setter:"_setConnector",value:null},fieldsDragConfig:{value:null,setter:"_setFieldsDragConfig",validator:s},graphic:{valueFn:function(){return new e.Graphic},validator:s},highlightDropZones:{validator:i,value:!0},strings:{value:{addNode:"Add node",cancel:"Cancel",close:"Close",deleteConnectorsMessage:"Are you sure you want to delete the selected connector(s)?",deleteNodesMessage:"Are you sure you want to delete the selected node(s)?",propertyName:"Property Name",save:"Save",settings:"Settings",value:"Value"}},showSuggestConnector:{validator:i,value:!0},suggestConnectorOverlay:{value:null,setter:"_setSuggestConnectorOverlay"}},EXTENDS:e.DiagramBuilderBase,FIELDS_TAB:0,SETTINGS_TAB:1,prototype:{editingConnector:null,editingNode:null,publishedSource:null,publishedTarget:null,selectedConnector:null,selectedNode:null,initializer:function(){var t=this;t.after({render:t.syncConnectionsUI}),t.on({cancel:t._onCancel,"drag:drag":t._onDrag,"drag:end":t._onDragEnd,"drop:hit":t._onDropHit,save:t._onSave}),e.DiagramNodeManager.on({publishedSource:function(e){t.publishedTarget=null,t.publishedSource=e.publishedSource}}),t.handlerKeyDown=e.getDoc().on(et,e.bind(t._afterKeyEvent,t)),t.dropContainer.delegate(b,e.bind(t._onNodeClick,t),Ht+Rt),t.dropContainer.delegate(it,e.bind(t._onNodeMouseEnter,t),Ht+Rt),t.dropContainer.delegate(st,e.bind(t._onNodeMouseLeave,t),Ht+Rt)},renderUI:function(){var t=this;e.DiagramBuilder.superclass.renderUI.apply(this,arguments),t._renderGraphic()},syncUI:function(){var t=this;e.DiagramBuilder.superclass.syncUI.apply(this,arguments),t._setupFieldsDrag(),t.connector=t.get(E)},syncConnectionsUI:function(){var e=this;e.get(W).each(function(e){e.syncConnectionsUI()})},clearFields:function(){var e=this,t=[];e.get(W).each(function(e){t.push(e)}),a.each(t,function(e){e.destroy()}),t=e.editingConnector=e.editingNode=e.selectedNode=null},closeEditProperties:function(){var t=this,n=t.editingNode,r=t.tabView;r.selectChild(e.DiagramBuilder.FIELDS_TAB),r.disableTab(e.DiagramBuilder.SETTINGS_TAB),n&&n.get(m).removeClass(zt),t.editingConnector=t.editingNode=null},connect:function(n,r,i){var s=this;return o(n)&&(n=e.DiagramNode.getNodeByName(n)),o(r)&&(r=e.DiagramNode.getNodeByName(r)),n&&r&&n.connect(r.get(t),i),s},connectAll:function(e){var t=this;return a.each(e,function(e){e.hasOwnProperty(yt)&&e.hasOwnProperty(Tt)&&t.connect(e.source,e.target,e.connector)}),t},createField:function(e){var t=this;return tn(e)||(e.builder=t,e.bubbleTargets=t,e=new(t.getFieldClass(e.type||ot))(e)),e},deleteSelectedConnectors:function(){var t=this,n=t.getStrings(),r=t.getSelectedConnectors();r.length&&confirm(n[O])&&(a.each(r,function(t){var n=t.get(Ct);e.DiagramNode.getNodeByName(n.source).disconnect(n)}),t.stopEditing())},deleteSelectedNode:function(){var e=this,t=e.getStrings(),n=e.selectedNode;n&&!n.get(ht)&&confirm(t[M])&&(n.close(),e.editingNode=e.selectedNode=null,e.stopEditing())},destructor:function(e){var t=this;t.get(xt).destroy()},eachConnector:function(e){var t=this;t.get(W).each(function(n){var r=n.get(kt);a.each(r.values(),function(r){e.call(t,n.getConnector(r),r,n)})})},editConnector:function(t){var n=this;if(t){var r=n.tabView;n.closeEditProperties(),r.enableTab(e.DiagramBuilder.SETTINGS_TAB),r.selectChild(e.DiagramBuilder.SETTINGS_TAB),n.propertyList.set(k,t.getProperties()),n.editingConnector=n.selectedConnector=t}},editNode:function(t){var n=this;if(t){var r=n.tabView;n.closeEditProperties(),r.enableTab(e.DiagramBuilder.SETTINGS_TAB),r.selectChild(e.DiagramBuilder.SETTINGS_TAB),n.propertyList.set(k,t.getProperties()),t.get(m).addClass(zt),n.editingNode=n.selectedNode=t}},getFieldClass:function(t){var n=this,r=e.DiagramBuilder.types[t];return r?r:(e.log("The field type: ["+t+"] couldn't be found."
),null)},getNodesByTransitionProperty:function(e,t){var n=this,r=[],i;return n.get(W).each(function(n){i=n.get(kt),a.each(i.values(),function(i){if(i[e]===t)return r.push(n),!1})}),r},getSelectedConnectors:function(){var e=this,t=[];return e.eachConnector(function(e){e.get(pt)&&t.push(e)}),t},getSourceNodes:function(e){var n=this;return n.getNodesByTransitionProperty(Tt,e.get(t))},hideSuggestConnetorOverlay:function(e,t){var n=this;n.connector.hide(),n.get(xt).hide();try{n.fieldsDrag.dd.set(nt,!1)}catch(r){}},isAbleToConnect:function(){var e=this;return!!e.publishedSource&&!!e.publishedTarget},isFieldsDrag:function(e){var t=this;return e===t.fieldsDrag.dd},plotField:function(e){var t=this;e.get(ct)||e.render(t.dropContainer)},select:function(e){var t=this;t.unselectNodes(),t.selectedNode=e.set(pt,!0).focus()},showSuggestConnetorOverlay:function(e){var t=this,n=t.get(xt),r=n.get(m);n.get(m).addClass($t),n.set(_t,e||t.connector.get(at)).show();try{t.fieldsDrag.dd.set(nt,!0)}catch(i){}},stopEditing:function(){var e=this;e.unselectConnectors(),e.unselectNodes(),e.closeEditProperties()},toJSON:function(){var e=this,t={nodes:[]};return e.get(W).each(function(e){var n={transitions:[]},r=e.get(kt);a.each(e.SERIALIZABLE_ATTRS,function(t){n[t]=e.get(t)}),a.each(r.values(),function(t){var r=e.getConnector(t);t.connector=r.toJSON(),n.transitions.push(t)}),t.nodes.push(n)}),t},unselectConnectors:function(){var e=this;a.each(e.getSelectedConnectors(),function(e){e.set(pt,!1)})},unselectNodes:function(){var e=this,t=e.selectedNode;t&&t.set(pt,!1),e.selectedNode=null},_afterKeyEvent:function(t){var n=this;if(t.hasModifier()||e.getDoc().get(f).test(":input,td"))return;t.isKey(U)?n._onEscKey(t):(t.isKey(p)||t.isKey(A))&&n._onDeleteKey(t)},_onCancel:function(e){var t=this;t.closeEditProperties()},_onDeleteKey:function(t){var n=this;tn(e.Widget.getByNode(t.target))&&(n.deleteSelectedConnectors(),n.deleteSelectedNode(),t.halt())},_onDrag:function(t){var n=this,r=t.target;if(n.isFieldsDrag(r)){var i=e.Widget.getByNode(r.get(F));i.alignTransitions(),a.each(n.getSourceNodes(i),function(e){e.alignTransitions()})}},_onDragEnd:function(t){var n=this,r=t.target,i=e.Widget.getByNode(r.get(F));i&&n.isFieldsDrag(r)&&i.set(_t,i.getLeftTop())},_onDropHit:function(e){var t=this,n=e.drag;if(t.isAvailableFieldsDrag(n)){var r=n.get(ot).getData(c),i=t.addField({xy:Gt(n.lastXY,t.dropContainer),type:r.get(Lt)});t.select(i)}},_onEscKey:function(e){var t=this;t.hideSuggestConnetorOverlay(),t.stopEditing(),e.halt()},_onCanvasMouseDown:function(e){var t=this;t.stopEditing(),t.hideSuggestConnetorOverlay()},_onNodeClick:function(t){var n=this,r=e.Widget.getByNode(t.currentTarget);n.select(r),n._onNodeEdit(t),t.stopPropagation()},_onNodeEdit:function(t){var n=this;if(!t.target.ancestor(Ht+Ut,!0))return;var r=e.Widget.getByNode(t.currentTarget);r&&n.editNode(r)},_onNodeMouseEnter:function(t){var n=this,r=e.Widget.getByNode(t.currentTarget);r.set(G,!0)},_onNodeMouseLeave:function(t){var n=this,r=n.publishedSource,i=e.Widget.getByNode(t.currentTarget);(!r||!r.boundaryDragDelegate.dd.get(I))&&i.set(G,!1)},_onSave:function(e){var t=this,n=t.editingNode,r=t.editingConnector,i=t.propertyList.get(k);n?i.each(function(e){n.set(e.get(l),e.get(At))}):r&&i.each(function(e){r.set(e.get(l),e.get(At))})},_onSuggestConnectorNodeClick:function(e){var t=this,n=e.currentTarget.getData(c),r=t.connector,i=t.addField({type:n.get(Lt),xy:r.toCoordinate(r.get(at))});t.hideSuggestConnetorOverlay(),t.publishedSource.connectNode(i)},_renderGraphic:function(){var t=this,n=t.get($),r=t.get(y);n.render(r),e.one(r).on(rt,e.bind(t._onCanvasMouseDown,t))},_setConnector:function(t){var n=this;if(!Yt(t)){var r=n.get(y).getXY();t=new e.Connector(e.merge({builder:n,graphic:n.get($),lazyDraw:!0,p1:r,p2:r,shapeHover:null,showName:!1},t))}return t},_setFieldsDragConfig:function(t){var n=this,r=n.dropContainer;return e.merge({bubbleTargets:n,container:r,dragConfig:{plugins:[{cfg:{constrain:r},fn:e.Plugin.DDConstrained},{cfg:{scrollDelay:150},fn:e.Plugin.DDWinScroll}]},nodes:Ht+Rt},t||{})},_setSuggestConnectorOverlay:function(t){var n=this;if(!t){var r=e.getDoc().invoke(C),i,s;a.each(n.get(h),function(e){var t=e.get(ot);r.appendChild(t.clone().setData(c,t.getData(c)))}),t=new e.Overlay({bodyContent:r,render:!0,visible:!1,width:280,zIndex:1e4}),i=t.get(m),s=t.get("contentBox"),s.addClass("popover-content"),i.addClass("popover"),i.delegate(b,e.bind(n._onSuggestConnectorNodeClick,n),Ht+qt)}return t},_setupFieldsDrag:function(){var t=this;t.fieldsDrag=new e.DD.Delegate(t.get(X))}}});e.DiagramBuilder=nn,e.DiagramBuilder.types={};var rn=e.Component.create({NAME:B,EXTENDS:e.Base});e.DiagramNodeManager=new rn;var sn=e.Component.create({NAME:j,UI_ATTRS:[G,t,ht,pt],ATTRS:{builder:{validator:en},connectors:{valueFn:"_connectorsValueFn",writeOnce:!0},controlsToolbar:{validator:s,valueFn:"_controlsToolbarValueFn"},description:{value:Bt,validator:o},graphic:{writeOnce:!0,validator:s},height:{value:60},highlighted:{validator:i,value:!1},name:{valueFn:function(){var t=this;return t.get(Lt)+ ++e.Env._uidx},validator:o},required:{value:!1,validator:i},selected:{value:!1,validator:i},shapeBoundary:{validator:s,valueFn:"_valueShapeBoundary"},highlightBoundaryStroke:{validator:s,value:{weight:7,color:"#484B4C",opacity:.25}},shapeInvite:{validator:s,value:{radius:12,type:"circle",stroke:{weight:6,color:"#ff6600",opacity:.8},fill:{color:"#ffd700",opacity:.8}}},strings:{value:{closeMessage:"Close",connectMessage:"Connect",description:"Description",editMessage:"Edit",name:"Name",type:"Type"}},tabIndex:{value:1},transitions:{value:null,writeOnce:!0,setter:"_setTransitions"},type:{value:ot,validator:o},width:{value:60},zIndex:{value:100}},EXTENDS:e.Overlay,CIRCLE_POINTS:[[35,20],[28,33],[14,34],[5,22],[10,9],[24,6],[34,16],[31,30],[18,35],[6,26],[7,12],[20,5],[33,12],[34,26],[22,35],[9,30],[6,16],[16,6],[30,9],[35,22],[26,34],[12,33],[5,20],[12,7],[26,6],[35,18],[30,31],[16,34],[6,24],[9,10],[22,5],[34,14
],[33,28],[20,35],[7,28],[6,14],[18,5],[31,10],[34,24],[24,34],[10,31],[5,18],[14,6],[28,8],[35,20],[28,33],[14,34],[5,22],[10,8],[25,6],[34,16],[31,30],[18,35],[6,26],[8,12],[20,5],[33,12],[33,27],[22,35],[8,30],[6,15],[16,6],[30,9],[35,23],[26,34],[12,32],[5,20],[12,7],[27,7],[35,18],[29,32],[15,34]],DIAMOND_POINTS:[[30,5],[35,10],[40,15],[45,20],[50,25],[55,30],[50,35],[45,40],[40,45],[35,50],[30,55],[25,50],[20,45],[15,40],[10,35],[5,30],[10,25],[15,20],[20,15],[25,10]],SQUARE_POINTS:[[5,5],[10,5],[15,5],[20,5],[25,5],[30,5],[35,5],[40,5],[50,5],[55,5],[60,5],[65,5],[65,10],[65,15],[65,20],[65,25],[65,30],[65,35],[65,40],[65,45],[65,50],[65,55],[65,60],[65,65],[60,65],[55,65],[50,65],[45,65],[40,65],[35,65],[30,65],[25,65],[20,65],[15,65],[10,65],[5,65],[5,60],[5,55],[5,50],[5,45],[5,40],[5,35],[5,30],[5,25],[5,20],[5,15],[5,10]],getNodeByName:function(t){return e.Widget.getByNode("[data-nodeId="+e.DiagramNode.buildNodeId(t)+"]")},buildNodeId:function(e){return H+jt+z+jt+e.replace(/[^a-z0-9.:_\-]/ig,"_")},prototype:{LABEL_TEMPLATE:'<div class="'+Wt+'">{label}</div>',boundary:null,hotPoints:[[0,0]],CONTROLS_TEMPLATE:'<div class="'+It+'"></div>',SERIALIZABLE_ATTRS:[_,t,ht,Lt,Mt,J,Dt,_t],initializer:function(){var t=this;t.after({"map:remove":e.bind(t._afterMapRemove,t),render:t._afterRender}),t.on({nameChange:t._onNameChange}),t.publish({connectDrop:{defaultFn:t.connectDrop},connectEnd:{defaultFn:t.connectEnd},connectMove:{defaultFn:t.connectMove},connectOutTarget:{defaultFn:t.connectOutTarget},connectOverTarget:{defaultFn:t.connectOverTarget},connectStart:{defaultFn:t.connectStart},boundaryMouseEnter:{},boundaryMouseLeave:{}}),t.get(m).addClass(Rt+Pt+t.get(Lt))},destructor:function(){var e=this;e.eachConnector(function(e,t,n){n.removeTransition(e.get(Ct))}),e.invite.destroy(),e.get($).destroy(),e.get(g).removeField(e)},addTransition:function(t){var n=this,r=n.get(kt);return t=n.prepareTransition(t),r.has(t.uid)||(t.uid=e.guid(),r.put(t.uid,t)),t},alignTransition:function(t){var n=this,r=e.DiagramNode.getNodeByName(t.target);if(r){var i=Qt(n,r);t=e.merge(t,{sourceXY:i[0],targetXY:i[1]}),n.getConnector(t).setAttrs({p1:Jt(n,t.sourceXY),p2:Jt(r,t.targetXY)})}},alignTransitions:function(){var t=this,n=t.get(kt);a.each(n.values(),e.bind(t.alignTransition,t))},close:function(){var e=this;return e.destroy()},connect:function(t,n){var r=this;t=r.addTransition(t);var i=null,s=e.DiagramNode.getNodeByName(t.target);if(s&&!r.isTransitionConnected(t)){var o=r.get(g),u=Qt(r,s);e.mix(t,{sourceXY:u[0],targetXY:u[1]}),i=new e.Connector(e.merge({builder:o,graphic:o.get($),transition:t},n)),r.get(S).put(t.uid,i)}return r.alignTransition(t),i},connectDrop:function(e){var t=this;t.connectNode(e.publishedTarget)},connectEnd:function(e){var t=this,n=e.target,r=t.get(g),i=r.publishedSource;!r.isAbleToConnect()&&r.get(gt)&&r.connector.get(Ot)?r.showSuggestConnetorOverlay():(r.connector.hide(),i.invite.set(Ot,!1)),r.get(Q)&&r.get(W).each(function(e){e.set(G,!1)})},connectMove:function(e){var t=this,n=t.get(g),r=e.mouseXY;n.connector.set(at,r);if(n.publishedTarget){var i=t.invite,s=i.get(lt)||0;i.get(Ot)||i.set(Ot,!0),i.setXY([r[0]-s,r[1]-s])}},connectNode:function(e){var n=this,r=n.boundaryDragDelegate.dd;n.connect(n.prepareTransition({sourceXY:Gt(r.startXY,n.get(m)),target:e.get(t),targetXY:Gt(r.mouseXY,e.get(m))}))},connectOutTarget:function(e){var t=this,n=t.get(g);n.publishedTarget=null,n.publishedSource.invite.set(Ot,!1)},connectOverTarget:function(e){var t=this,n=t.get(g);n.publishedSource!==t&&(n.publishedTarget=t)},connectStart:function(t){var n=this,r=n.get(g),i=r.get(y);r.connector.show().set(ut,t.startXY),r.get(Q)&&r.get(W).each(function(e){e.set(G,!0)}),e.DiagramNodeManager.fire("publishedSource",{publishedSource:n})},disconnect:function(e){var t=this;t.isTransitionConnected(e)&&t.removeTransition(e)},eachConnector:function(e){var n=this,r=[],i=[].concat(n.get(S).values()),s=i.length;return a.each(n.get(g).getSourceNodes(n),function(e){var s=e.get(S);a.each(s.values(),function(s){n.get(t)===s.get(Ct).target&&(r.push(e),i.push(s))})}),a.each(i,function(t,i){e.call(n,t,i,i<s?n:r[i-s])}),i=r=null,i},getConnector:function(e){var t=this;return t.get(S).getValue(e.uid)},getContainer:function(){var e=this;return e.get(g).dropContainer||e.get(m).get(ft)},getLeftTop:function(){var e=this;return Gt(e.get(m),e.getContainer())},getProperties:function(){var e=this,t=e.getPropertyModel();return a.each(t,function(t){var r=e.get(t.attributeName),i=n.type(r);i===d&&(r=String(r)),t.value=r}),t},getPropertyModel:function(){var n=this,r=n.getStrings();return[{attributeName:_,editor:new e.TextAreaCellEditor,name:r[_]},{attributeName:t,editor:new e.TextCellEditor({validator:{rules:{value:{required:!0}}}}),name:r[t]},{attributeName:Lt,editor:!1,name:r[Lt]}]},isBoundaryDrag:function(e){var t=this;return e===t.boundaryDragDelegate.dd},isTransitionConnected:function(e){var t=this;return t.get(S).has(e.uid)},prepareTransition:function(n){var r=this,i={source:r.get(t),target:null,uid:e.guid()};return o(n)?i.target=n:s(n)&&(i=e.merge(i,n)),i},removeTransition:function(e){var t=this;return t.get(kt).remove(e.uid)},renderShapeBoundary:function(){var e=this,t=e.boundary=e.get($).addShape(e.get(vt));return t},renderShapeInvite:function(){var e=this,t=e.invite=e.get(g).get($).addShape(e.get(mt));return t.set(Ot,!1),t},syncConnectionsUI:function(){var e=this,t=e.get(kt);a.each(t.values(),function(t){e.connect(t,t.connector)})},_afterConnectorRemove:function(e){var t=this;e.value.destroy()},_afterRender:function(e){var t=this;t.setStdModContent(u.BODY,Bt,u.AFTER),t._renderGraphic(),t._renderControls(),t._renderLabel(),t._uiSetHighlighted(t.get(G))},_afterTransitionsRemove:function(e){var t=this;t.get(S).remove(e.value.uid)},_bindBoundaryEvents:function(){var t=this;t.boundary.detachAll().on({mouseenter:e.bind(t._onBoundaryMouseEnter,t),mouseleave:e.bind(t._onBoundaryMouseLeave,t)})},_connectorsValueFn:function(t){var n=this;return new 
e.Map({after:{remove:e.bind(n._afterConnectorRemove,n)}})},_controlsToolbarValueFn:function(t){var n=this,r=n.get(Y);return{children:[{icon:"icon-remove",on:{click:e.bind(n._handleCloseEvent,n)}}]}},_handleCloseEvent:function(e){var t=this;t.get(g).deleteSelectedNode()},_handleConnectStart:function(e){var t=this;t.fire("connectStart",{startXY:e})},_handleConnectMove:function(e){var t=this,n=t.get(g);t.fire("connectMove",{mouseXY:e,publishedSource:n.publishedSource})},_handleConnectEnd:function(){var e=this,t=e.get(g),n=t.publishedSource,r=t.publishedTarget;n&&r&&e.fire("connectDrop",{publishedSource:n,publishedTarget:r}),e.fire("connectEnd",{publishedSource:n})},_handleConnectOutTarget:function(){var e=this,t=e.get(g),n=t.publishedSource;n&&e.fire("connectOutTarget",{publishedSource:n})},_handleConnectOverTarget:function(){var e=this,t=e.get(g),n=t.publishedSource;n&&e.fire("connectOverTarget",{publishedSource:n})},_onBoundaryDrag:function(e){var t=this,n=t.boundaryDragDelegate.dd;t._handleConnectMove(n.con._checkRegion(n.mouseXY))},_onBoundaryDragEnd:function(e){var t=this;t._handleConnectEnd(),e.target.get(F).show()},_onBoundaryDragStart:function(e){var t=this;t._handleConnectStart(t.boundaryDragDelegate.dd.startXY),e.target.get(F).hide()},_onBoundaryMouseEnter:function(e){var t=this;t.fire("boundaryMouseEnter",{domEvent:e}),t._handleConnectOverTarget()},_onBoundaryMouseLeave:function(e){var t=this;t.fire("boundaryMouseLeave",{domEvent:e}),t._handleConnectOutTarget()},_onNameChange:function(e){var t=this;t.eachConnector(function(n,r,i){var s=n.get(Ct);s[t===i?yt:Tt]=e.newVal,n.set(Ct,s)})},_renderControls:function(){var t=this,n=t.get(m);t.controlsNode=e.Node.create(t.CONTROLS_TEMPLATE).appendTo(n)},_renderControlsToolbar:function(t){var n=this;n.controlsToolbar=(new e.Toolbar(n.get(N))).render(n.controlsNode),n._uiSetRequired(n.get(ht))},_renderGraphic:function(){var t=this;t.set($,new e.Graphic({height:t.get(J),render:t.bodyNode,width:t.get(Mt)})),t.renderShapeInvite(),t.renderShapeBoundary().addClass(Vt),t._bindBoundaryEvents(),t._setupBoundaryDrag()},_renderLabel:function(){var t=this;t.labelNode=e.Node.create(n.sub(t.LABEL_TEMPLATE,{label:t.get("name")})),t.get("contentBox").placeAfter(t.labelNode)},_setTransitions:function(t){var n=this;if(!Zt(t)){var r=new e.Map({after:{remove:e.bind(n._afterTransitionsRemove,n)}});e.Array.each(t,function(t){var i=e.guid();t=s(t)?e.mix(t,{uid:i}):{uid:i,target:t},r.put(i,n.prepareTransition(t))}),t=r}return t},_setupBoundaryDrag:function(){var t=this,n=t.get(g);t.boundaryDragDelegate=new e.DD.Delegate({bubbleTargets:t,container:t.bodyNode,nodes:Ht+Vt,dragConfig:{useShim:!1,plugins:[{cfg:{constrain:n?n.get(y):null},fn:e.Plugin.DDConstrained},{cfg:{scrollDelay:150},fn:e.Plugin.DDWinScroll},{cfg:{borderStyle:"0px",moveOnEnd:!1,resizeFrame:!1},fn:e.Plugin.DDProxy}]},on:{"drag:drag":e.bind(t._onBoundaryDrag,t),"drag:end":e.bind(t._onBoundaryDragEnd,t),"drag:start":e.bind(t._onBoundaryDragStart,t)}}),e.Do.after(t._bindBoundaryEvents,t.boundaryDragDelegate.dd,"_unprep",t)},_uiSetHighlighted:function(e){var t=this;if(t.get(ct)){var n=e?t.get(K):t.get(vt+Ht+Et);n&&t.boundary.set(Et,n)}},_uiSetName:function(t){var n=this,r=n.get(m);r.setAttribute(L,e.DiagramNode.buildNodeId(t)),n.get("rendered")&&n.labelNode.setContent(t)},_uiSetRequired:function(e){var t=this,n=t.controlsToolbar;n},_uiSetSelected:function(e){var t=this;t.get(m).toggleClass(Xt,e),e&&!t.controlsToolbar&&t._renderControlsToolbar()},_uiSetXY:function(e){var t=this,n=t.getContainer().getXY();this._posNode.setXY([e[0]+n[0],e[1]+n[1]])},_valueShapeBoundary:function(){var e=this;return{height:41,type:"rect",stroke:{weight:7,color:"transparent",opacity:0},width:41}}}});e.DiagramNode=sn,e.DiagramBuilder.types[ot]=e.DiagramNode,e.DiagramNodeState=e.Component.create({NAME:j,ATTRS:{height:{value:40},type:{value:wt},width:{value:40}},EXTENDS:e.DiagramNode,prototype:{hotPoints:e.DiagramNode.CIRCLE_POINTS,renderShapeBoundary:function(){var e=this,t=e.boundary=e.get($).addShape(e.get(vt));return t.translate(5,5),t},_valueShapeBoundary:function(){var e=this;return{radius:15,type:"circle",stroke:{weight:7,color:"transparent",opacity:0}}}}}),e.DiagramBuilder.types[wt]=e.DiagramNodeState,e.DiagramNodeCondition=e.Component.create({NAME:j,ATTRS:{height:{value:60},type:{value:w},width:{value:60}},EXTENDS:e.DiagramNodeState,prototype:{hotPoints:e.DiagramNode.DIAMOND_POINTS,renderShapeBoundary:function(){var e=this,t=e.boundary=e.get($).addShape(e.get(vt));return t.translate(10,10),t.rotate(45),t},_valueShapeBoundary:e.DiagramNode.prototype._valueShapeBoundary}}),e.DiagramBuilder.types[w]=e.DiagramNodeCondition,e.DiagramNodeStart=e.Component.create({NAME:j,ATTRS:{type:{value:bt}},EXTENDS:e.DiagramNodeState}),e.DiagramBuilder.types[bt]=e.DiagramNodeStart,e.DiagramNodeEnd=e.Component.create({NAME:j,ATTRS:{type:{value:R}},EXTENDS:e.DiagramNodeState}),e.DiagramBuilder.types[R]=e.DiagramNodeEnd,e.DiagramNodeJoin=e.Component.create({NAME:j,ATTRS:{height:{value:60},type:{value:Z},width:{value:60}},EXTENDS:e.DiagramNodeState,prototype:{hotPoints:e.DiagramNode.DIAMOND_POINTS,renderShapeBoundary:e.DiagramNodeCondition.prototype.renderShapeBoundary,_valueShapeBoundary:e.DiagramNode.prototype._valueShapeBoundary}}),e.DiagramBuilder.types[Z]=e.DiagramNodeJoin,e.DiagramNodeFork=e.Component.create({NAME:j,ATTRS:{height:{value:60},type:{value:V},width:{value:60}},EXTENDS:e.DiagramNodeState,prototype:{hotPoints:e.DiagramNode.DIAMOND_POINTS,renderShapeBoundary:e.DiagramNodeCondition.prototype.renderShapeBoundary,_valueShapeBoundary:e.DiagramNode.prototype._valueShapeBoundary}}),e.DiagramBuilder.types[V]=e.DiagramNodeFork,e.DiagramNodeTask=e.Component.create({NAME:j,ATTRS:{height:{value:70},type:{value:Nt},width:{value:70}},EXTENDS:e.DiagramNodeState,prototype:{hotPoints:e.DiagramNode.SQUARE_POINTS,renderShapeBoundary:function(){var e=this,t=e.boundary=e.get($).addShape(e.get(vt));return t.translate(8,8),t},_valueShapeBoundary:function(
){var e=this;return{height:55,type:"rect",stroke:{weight:7,color:"transparent",opacity:0},width:55}}}}),e.DiagramBuilder.types[Nt]=e.DiagramNodeTask},"2.0.0",{requires:["overlay","aui-map","aui-diagram-builder-base","aui-diagram-builder-connector"],skinnable:!0});