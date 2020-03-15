/**
 * 新增和编辑服务
 * 抽象新增和编辑功能
 */
;(function (window, $) {
    let OperateServer = function (option) {
        let _self = this;
        // 渲染区域的div
        this.operateDiv = null;
        this.isEdit = false;
        this.title = this.isEdit ? "编辑" : "新增";
        this.submitUrl = 'save';
        this.btnOption = [{
            name: '提交',
            type: 'submit',
            class: 'am-btn am-btn-primary',
            action: function (def, formData) {
                Api.post(_self.submitUrl, formData).then(function (resp) {
                    Message.success(resp.message);
                    tableServer.resetAndRefreshTableData();
                    def.done();
                });
            }
        }];
        this.init(option);
    };
    OperateServer.prototype = {
        outerDiv: null,
        form: null,
        init: function (option) {
            option = option || {};
            $.extend(this, option);
        },
        /**
         * 主构建流程
         * @param cfg
         * 配置结构: key:参数名 label:文本值 type:表单类型 options下拉选项(code:xxx name:xxx)
         */
        build: function (cfg) {
            if (ComUtils.isNotArray(cfg)) {
                return;
            }
            // 生成外层基本结构 结构可将文本拷贝值ide emmet语法补全 或参照下方注释
            this.outerDiv = $.e('div#operateDiv.am-modal.am-modal-no-btn[tabindex="-1"]>' +
                'div.am-modal-dialog>div.am-modal-hd>{' + this.title + '}+a.am-close.am-close-spin[data-am-modal-close]>{&times;}^^+' +
                'div.am-modal-bd>div.row-content.am-cf>div.row>div.am-u-sm-12.am-u-md-12.am-u-lg-12>div.widget.am-cf>div.widget-body.am-fr>' +
                'form.am-form.tpl-form-border-form.tpl-form-border-br');
            this.form = this.outerDiv.find('form');
            this.buildForm(this.form, cfg, this.btnOption);
            this.operateDiv.append(this.outerDiv);
            // 初始化校验
            this.form.validator({
                onInValid: function (validity) {
                    let field = $(validity.field);
                    let msg = field.data('validationMessage') || this.getValidationMessage(validity);
                    Message.error(msg);
                }
            });
        },
        /**
         * 构建表单
         *
         * @param form 表单jq对象
         * @param cfg 构建配置
         * @param btnOption 按钮配置
         */
        buildForm: function (form, cfg, btnOption) {
            let _self = this;
            form.empty();
            cfg.forEach(function (item) {
                let rowDiv = $.e('div.am-u-sm-12.am-u-md-12.am-u-lg-12.am-form-group');
                rowDiv.append($.e('label.am-u-sm-3.am-form-label[for="' + item.key + '"]>{' + item.label + '}'));
                $.e('div.am-u-sm-9').appendTo(rowDiv).append(_self._buildEditor(item));
                form.append(rowDiv);
            });
            // 追加按钮
            btnOption.forEach(function (item) {
                let button = $.e('button.' + item.class.split(' ').join('.') + '[type="button"]>{' + item.name + '}');
                button.click(function () {
                    // 如果按钮类型为submit  创建一个钩子用于校验 使调用方省去校验的过程
                    if (item.type === 'submit') {
                        _self.submit(item.action);
                    } else {
                        item.action();
                    }
                });
                form.append(button);
            });
        },
        submit: function (fun) {
            let _self = this;
            let checkStatus = this.form.validator('isFormValid');
            if (checkStatus) {
                let def = $.Deferred();
                let defParam = {
                    done: function () {
                        def.resolve();
                    }
                };
                // 传递两个参数 表单数据,def对象(用于传递消息以关闭弹窗)
                fun(defParam, JqueryUtils.getFormData(this.form));
                def.then(function () {
                    _self.toggle();
                });
            } else {
                Message.error("请完善表单信息!");
            }
        },
        /**
         * 弹窗状态取反
         */
        toggle: function () {
            this.outerDiv.modal('toggle');
        },
        /**
         * 打开新增弹窗
         *
         * @param submitUrl 提交的url
         * @param title 标题
         */
        openAdd: function (submitUrl, title) {
            this.submitUrl = submitUrl || 'save';
            this.title = title || '新增';
            this.changeTitle(this.title);
            this.form.find('input').val('');
            this.toggle();
        },
        /**
         * 打开编辑弹窗
         *
         * @param data 回显数据
         * @param submitUrl 提交的url
         * @param title 标题
         */
        openEdit: function (data, submitUrl, title) {
            let _self = this;
            this.isEdit = true;
            this.submitUrl = submitUrl || 'update';
            this.title = title || '编辑';
            this.changeTitle(this.title);
            // 追加id列
            if (this.form.find('input[name="id"]').length === 0) {
                this.form.append($.e('input[type="hidden"][name="id"]'));
            }
            Object.keys(data).forEach(function (fieldName) {
                let editor = _self.form.find('[name="' + fieldName + '"]');
                if (editor.length === 0) {
                    return;
                }
                editor.val(data[fieldName]);
            });
            this.toggle();
        },
        /**
         * 修改标题
         *
         * @param title 标题
         */
        changeTitle: function (title) {
            $('div.am-modal-dialog .am-modal-hd').text(title);
        },
        /**
         * 构建输入器
         * @param cfg 字段配置
         */
        _buildEditor: function (cfg) {
            let type = cfg.type || 'text';
            switch (type) {
                case 'text':
                    return $.e('input#' + cfg.key + '.tpl-form-input[type="text"][name="' + cfg.key + '"]'
                        + (cfg.required ? '[required]' : '')
                        + (cfg.errorMsg ? '[data-validation-message="' + cfg.errorMsg + '"]' : '[data-validation-message="' + cfg.label + '有误"]'));
                case 'select':
                    break;
                default:
            }
        }
    };
    window.OperateServer = OperateServer;
})(window, jQuery);

// <div class="am-modal am-modal-no-btn" tabindex="-1" id="add">
//         <div class="am-modal-dialog">
//             <div class="am-modal-hd">角色新增
//                 <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
//             </div>
//             <div class="am-modal-bd">
//                 <div class="row-content am-cf">
//                     <div class="row">
//                         <div class="am-u-sm-12 am-u-md-12 am-u-lg-12">
//                             <div class="widget am-cf">
//                                 <div class="widget-body am-fr">
//                                     <form class="am-form tpl-form-border-form tpl-form-border-br" id="actionForm">
//                                         <div class="am-u-sm-12 am-u-md-12 am-u-lg-12 am-form-group">
//                                             <label for="roleName" class="am-u-sm-3 am-form-label">角色名</label>
//                                             <div class="am-u-sm-9">
//                                                 <input type="text" class="tpl-form-input" id="roleName" name="roleName"
//                                                        data-validation-message="角色名有误" required>
//                                             </div>
//                                         </div>
//
//                                         <div class="am-u-sm-12 am-form-group">
//                                             <label for="desc" class="am-u-sm-3 am-form-label">角色描述 </label>
//                                             <div class="am-u-sm-9">
//                                                 <input type="text" class="tpl-form-input" id="desc" name="desc"
//                                                        data-validation-message="角色描述有误" required>
//                                             </div>
//                                         </div>
//                                         <button type="button" class="am-btn am-btn-primary" onclick="save(this)">
//                                             提交
//                                         </button>
//                                     </form>
//                                 </div>
//                             </div>
//                         </div>
//                     </div>
//                 </div>
//             </div>
//         </div>
//     </div>