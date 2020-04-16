/**
 * 通用的dataTables 配置工具类
 * 在通用的dataTables的基础上追加了一些配置
 * 依赖: comUtils.js jquery.emmet.js
 */
;(function (window, $, undefined) {
    let TableServer = function (option) {
        if (!ComUtils) {
            throw Error("请引入comUtils.js");
        }
        //生成的表格对象
        this.table = {};
        this.tableDiv = null;
        //用于存储搜索条件区域的div 传入jquery对象
        this.searchDiv = null;
        //请求表格数据的url
        this.tableDataUrl = '';
        this.actionOption = [{
            name: '新增',
            icon: 'am-icon-plus',
            // url or function
            action: function () {
                if (operateServer) {
                    operateServer.openAdd();
                } else {
                    $('#operateDiv').modal('toggle');
                }
            }
        }];
        this.dataTableOption = null;
        //是否开启字段查询配置 开启则会对 columnsOption传入的 searchAble属性进行处理
        // (如果值为true默认创建一个文本查询框 默认id为字段data值,否则需要是一个配置对象)
        // 此属性会导致初始化的columnsOption属性失效
        this.columnsSearch = false;
        //默认的下拉框选项文本
        this.defaultSelectText = '---请选择---';
        //是否开启字段选择
        this.openChoose = false;
        //选择框不可选的条件
        this.chooseDisable = function (rowData) {
            return false;
        };
        //选择框默认选中的条件
        this.chooseChecked = function (rowData) {
            return false;
        };
        this.extraParams = null;
        this.init(option);
    };
    TableServer.prototype = {
        dataTable: null,
        searchOptions: null,
        //表字段配置
        columnsOption: null,
        //是否存在下拉多选框
        signForMultipleSelect: false,
        //初始化参数
        init: function (option) {
            option = option || {};
            $.extend(this, option);
            return this;
        },
        //构建表格对象
        build: function (columnsOption, dataTableOption) {
            if (dataTableOption && !this.dataTableOption) {
                this.dataTableOption = {};
                $.extend(this.dataTableOption, dataTableOption);
            }
            if (!this.tableDiv || !columnsOption || !ComUtils.isArray(columnsOption)) {
                console.log("表格配置有误！！！");
                return;
            }
            this.columnsOption = columnsOption;
            //创建表格对象
            let table = this._createTableAndTh();
            //处理配置信息
            this._extractAndFillColumnsOption();
            //排序并自动创建查询区域
            this._sortedAndAutoCreateSearchDiv();
            //创建dataTable对象
            this._createDataTable(table);
            return this;
        },
        //创建查询条件对象 option为数组 [{type: 'select',key: 'nameCn',label: '产品名称'}]
        //可选属性 options: [code:'',name:''] 对应select下拉选项
        buildSearch: function (options) {
            if (!this.searchDiv) return;
            options = options || this.searchOptions;
            //生成的容器
            let searchDiv = $('<div></div>');
            // 构建操作按钮
            searchDiv.append(this._buildActionButton());
            // 用于循环的结果div
            let cycleDiv;
            for (let i = 0; i < options.length; i++) {
                cycleDiv = $.e('div.am-u-sm-12.am-u-md-6.am-u-lg-3');
                let option = options[i];
                let outerDiv;
                switch (option.type ? option.type : 'text') {
                    case 'text':
                        outerDiv = $.e('div.am-form-group.am-input-group-sm.tpl-form-border-form.cl-p');
                        outerDiv.append('<input type="text" name="' + option.key + '" class="am-form-field " placeholder="' + option.label + '"/>');
                        break;
                    case 'select':
                        outerDiv = $.e('div.am-form-group.tpl-table-list-select');
                        let select = $('<select name="' + option.key + '" title="' + option.label + '" data-am-selected="{btnSize: \'sm\'}"><option value="">' + this.defaultSelectText + '</option></select>');
                        select.appendTo(outerDiv);
                        if (option.options) {
                            $.each(option.options, function (index, val) {
                                select.append('<option value="' + val.code + '">' + val.name + ' </option>')
                            })
                        }
                        break;
                    case 'date':
                        break;
                    case 'dateTime':
                        break;
                    case 'dateRange':
                        break;
                    case 'dateTimeRange':
                        break;
                    case 'multipleSelect':
                        break;
                    default:
                        console.log('配置有误,跳过生成');
                }
                outerDiv.appendTo(cycleDiv);
                cycleDiv.appendTo(searchDiv);
                let _self = this;
                // 第三个查询框时,或者 已经是最后一个但长度还小于2时加入查询按钮
                if (i === 2 || (i === options.length - 1 && i < 2)) {
                    // 追加查询按钮
                    let searchBtnDiv = $.e('div.am-u-sm-12.am-u-md-12.am-u-lg-1>div.am-input-group-sm.tpl-form-border-form.cl-p');
                    searchBtnDiv.children('.am-input-group-sm')
                        .append($.e('span.am-input-group-btn>button.am-btn.am-btn-default.am-btn-success.tpl-table-list-field>{查询}'));
                    searchBtnDiv.find('button').click(function () {
                        _self.resetAndRefreshTableData();
                    });
                    searchBtnDiv.appendTo(searchDiv);
                }
            }
            this.searchOptions = options;
            searchDiv.appendTo(this.searchDiv.empty());
        },
        //从数据字典获取下拉列表并填充数据
        // 参数 option 为需要填充的集合
        // 允许两种传输方式
        // 1 (逗号分隔 例如:nameCn,sex) 如此例 则查询的枚举表dicType为 NAME_CN和SEX 返回时key分别为nameCn sex 设置到$(`[name="${nameCn}"]`) 上
        // 2 (数组形式 例如:[{key:'nameCn',dicKey:'nameEn']}) 如此例 则查询的枚举表dicType为NAME_CN 返回时key为nameCn 设置到$(`[name="${nameCn}"]`) 上
        fillSelectByDicData: function (option, preStr) {
            if (!option) return;
            preStr = preStr || '';
            //映射dicKey和实际的key
            let mapObj;
            let paramStr = '';
            if (ComUtils.isArray(option)) {
                mapObj = {};
                option.forEach(function (val, index) {
                    mapObj[val.dicKey] = val.key;
                    paramStr += val.dicKey + ","
                });
                paramStr = paramStr.substring(0, paramStr.length - 1);
            } else {
                paramStr = option;
            }
            let _self = this;
            $.post("/comm/dicData/queryDicData", {
                preStr: preStr,
                searchKeys: paramStr
            }).then(function (resp) {
                if (resp) {
                    $.each(resp, function (key, val) {
                        let thisSelect;
                        if (mapObj) {
                            thisSelect = _self.searchDiv.find('[name="' + mapObj[key] + '"]');
                        } else {
                            thisSelect = _self.searchDiv.find('[name="' + key + '"]');
                        }
                        if (val) {
                            val.forEach(function (val, index) {
                                $('<option value="' + val.code + '">' + val.name + ' </option>').appendTo(thisSelect);
                            })
                        }
                    })
                }
            })
        },
        //刷新表格数据
        resetAndRefreshTableData: function () {
            let searchCondition = this.getSearchData();
            if (this.extraParams) {
                $.extend(searchCondition, this.extraParams);
            }
            this.dataTable.settings()[0].ajax.data = searchCondition;
            this.dataTable.ajax.reload();
        },
        refreshTableData: function () {
            this.dataTable.ajax.reload();
        },
        //获取查询区域数据
        getSearchData: function () {
            let queryParam = {};
            this.searchDiv.find(":input").each(function () {
                let $this = $(this);
                if (!($this.val() instanceof Array)) {
                    $this.val($.trim($this.val()));
                    // 为空字符串则跳过此次循环
                    if (!$this.val()) {
                        return true;
                    }
                    if ($this.attr("name").endWith("End")) {
                        queryParam[$this.attr("name")] = $this.val().substring(0, $this.val().lastIndexOf('-') + 1) + (parseInt($this.val().split('-')[2]) + 1);
                    } else {
                        queryParam[$this.attr("name")] = $this.val();
                    }
                } else {
                    queryParam[$this.attr("name")] = $this.val() ? $this.val().join(',') : '';
                }
            });
            return queryParam;
        },
        //获取行数据
        getTableRowData: function (tag) {
            if (tag.tagName && tag.tagName === "TR") {
                return this.dataTable.row(tag).data();
            } else {
                let trTag = $(tag).parents('tr')[0];
                return this.dataTable.row(trTag).data();
            }
        },
        //获取中数据
        getChooseData: function (convertFun) {
            let scrollBodyFindCondition = '.dataTables_scroll';
            let returnArr = [];
            let _self = this;
            $(scrollBodyFindCondition + ' [name="selectBox"]:not(:disabled):checked').each(function (i) {
                let rowData = _self.getTableRowData($(this));
                if (rowData) {
                    returnArr.push(convertFun && ComUtils.isFunction(convertFun) ? convertFun(rowData) : rowData);
                }
            });
            return returnArr;
        },
        clearSearchArea: function () {
            this.searchDiv.find(":input").each(function () {
                let $this = $(this);
                $this.val("");
            });
        },
        //获得dataTable对象
        getDataTable: function () {
            return this.dataTable;
        },
        //追加查询区域配置
        addSearchOptions: function (option, index) {
            if (ComUtils.isNotObjectAndArray(option)) {
                console.log("追加查询配置有误");
                return;
            }
            let options = ComUtils.isObject(option) ? [option] : option;
            //不传下标或下标大于数组长度则拼接在尾部
            if (!index || index > this.searchOptions.length) {
                this.searchOptions = this.searchOptions.concat(options);
            } else {
                //否则拼接在指定位置
                this.searchOptions = this.searchOptions.slice(0, index).concat(options).concat(this.searchOptions.slice(index));
            }
        },
        /**
         * 传教表格对象和th
         */
        _createTableAndTh: function () {
            let table = $('<table style="width: 100%;" id="tableComponent" class="am-table am-table-compact am-table-striped tpl-table-black"/>').appendTo(this.tableDiv);
            // 存在此属性则渲染父表头
            if (!this.columnsOption.find(function (item) {
                return !!item.pTitle
            })) {
                return table;
            }
            let thead = $("<thead></thead>").appendTo(table);
            let pTr = $("<tr></tr>").appendTo(thead);
            let tr = $("<tr></tr>").appendTo(thead);
            // 父表头计数器
            let pTrCounter = 0;
            // 搜集配置中的title信息
            this.columnsOption.forEach(function (item) {
                if (pTrCounter === 0) {
                    if (item.pTitle) {
                        let pTitle = item.pTitle;
                        pTrCounter = pTitle.colspan - 1;
                        pTr.append("<th colspan='" + pTitle.colspan + "'>" + pTitle.title + "</th>")
                        tr.append("<th>" + item.title + "</th>")
                        pTrCounter --;
                    } else {
                        pTr.append("<th colspan='1' rowspan='2'>" + item.title + "</th>");
                    }
                }
            });
            return table;
        },
        _sortedAndAutoCreateSearchDiv: function () {
            //创建查询区域
            if (this.columnsSearch) {
                //先渲染设置排序值的 在渲染未设值的 (未设值的不做排序处理)
                let waitOptions = this.searchOptions.filter(function (item) {
                    return !!item.sort
                });
                if (waitOptions.length !== 0) {
                    //找出不需要排序的数据和需要排序的数据
                    let unSortOption = this.searchOptions.filter(function (item) {
                        return !item.sort
                    });
                    this.searchOptions = waitOptions.sort(function (a, b) {
                        return a.sort - b.sort
                    }).concat(unSortOption);
                }
                //将事件发布出去 允许自行追加搜索区域 这个监听要在build之前
                jQuery(this).trigger("beforeSearchAreaCreate", [this.searchOptions]);
                //拼接之后进行渲染
                this.buildSearch(this.searchOptions);
            }
        },
        //抽取和填充字段配置  这里加入一个searchable配置,和wrap配置(dataTable不需要的配置)
        _extractAndFillColumnsOption: function () {
            let _self = this;
            if (this.columnsSearch) {
                //重置为新数组
                this.searchOptions = [];
                //填充默认值 同时收集查询条件必要的属性
                this.columnsOption.forEach(function (item) {
                    _self._processText(item);
                    // 根据配置 增加单元格标题
                    _self._processCellTitle(item);
                    if (item.searchable) {
                        if (item.searchable === true) {
                            //默认赋予一个文本框查询条件
                            _self.searchOptions.push({
                                key: item.data,
                                label: item.title,
                                type: 'text'
                            })
                        } else {
                            //否则继承searchable属性
                            let colOption = {};
                            $.extend(colOption, item.searchable);
                            colOption.key = colOption.key || item.data;
                            colOption.label = colOption.label || item.title;
                            colOption.type = colOption.type || 'text';
                            _self.searchOptions.push(colOption);
                        }
                        //删除dataTable无用配置
                        delete item.searchable;
                    }
                });
            } else {
                //填充默认值
                this.columnsOption.forEach(function (item) {
                    _self._processText(item);
                    // 根据配置 增加单元格标题
                    _self._processCellTitle(item);
                });
            }

            //加入序号列
            if (this.columnsOption[0].title !== '序号') {
                this.columnsOption.unshift({
                    "title": "序号",
                    "data": null,
                    "render": function (data, type, row, meta) {
                        return meta.row + meta.settings._iDisplayStart + 1;
                    }
                });
            }

            // 加入选择列
            if (this.openChoose) {
                this.columnsOption.unshift({
                    "title": '<label><input type="checkbox" name="allCheck" value="全选"/></label>',
                    "className": "text-center",
                    "data": 'id',
                    "createdCell": function (td, cellData, rowData, row, col) {
                        let checkbox = $("<input type='checkbox' name='selectBox' value='" + cellData + "' />");
                        if (_self.chooseDisable(rowData)) {
                            checkbox.prop('disabled', true);
                        } else {
                            if (_self.chooseChecked(rowData)) {
                                checkbox.prop('checked', true);
                            }
                        }
                        $(td).html(checkbox);
                    }
                });
            }

        },
        // 处理文本
        _processText: function (item) {
            let _self = this;
            if (!item.defaultContent) {
                item.defaultContent = "";
            }
            //自动填充换行逻辑
            if (item.wrap) {
                if (ComUtils.isNumber(item.wrap)) {
                    item.render = function (data, type, row) {
                        return _self._textConversionWrapDiv(data, item.wrap);
                    }
                } else if (ComUtils.isBoolean(item.wrap)) {
                    item.render = function (data, type, row) {
                        return _self._textConversionWrapDiv(data);
                    }
                }
                delete item.wrap;
            }
            if (item.textBreak) {
                if (ComUtils.isNumber(item.textBreak)) {
                    item.render = function (data, type, row) {
                        return _self._textBreakDiv(data, item.textBreak);
                    }
                } else if (ComUtils.isBoolean(item.textBreak)) {
                    item.render = function (data, type, row) {
                        return _self._textBreakDiv(data);
                    }
                }
                delete item.textBreak;
            }
        },
        // 处理单元格title
        _processCellTitle: function (item) {
            if (!item.cellTitle) {
                return;
            }
            let customCreatedCellFun = item.createdCell;
            item.createdCell = function (td, cellData, rowData, rowIndex, colIndex) {
                $(td).attr('title', ComUtils.isBoolean(item.cellTitle) ? (cellData || '') : item.cellTitle);
                // 调用原有的自定义方法
                if (customCreatedCellFun && ComUtils.isFunction(customCreatedCellFun)) {
                    customCreatedCellFun(td, cellData, rowData, rowIndex, colIndex);
                }
            }
        },
        _createDataTable: function (tableTag) {
            if (!this.dataTable) {
                let _self = this;
                //默认的dataTable配置
                let defaultDataTableOption = {
                    ordering: false,
                    dom: 'rt<"tablepage"lpi>',
                    scrollCollapse: false,
                    processing: true,
                    sScrollX: true,
                    serverSide: true, stateSave: true,
                    paging: true,
                    fixedColumns: false,
                    columns: this.columnsOption,
                    oLanguage: {
                        "sSearch": "快速搜索:",
                        "sProcessing": "努力加载数据中......",
                        "bAutoWidth": true,
                        "sLengthMenu": "每页显示 _MENU_ 条记录",
                        "sZeroRecords": "没有记录",
                        "sInfo": "_START_ 到 _END_ 条,共 _TOTAL_ 条",
                        "sInfoEmpty": "显示0条记录",
                        "sInfoFiltered": "(从 _MAX_ 条中过滤)",
                        "oPaginate": {
                            "sPrevious": "<上一页",
                            "sNext": "下一页>",
                            "sLast": "末页",
                            "sFirst": "首页"
                        }
                    },
                    ajax: {
                        "type": "POST",
                        "url": this.tableDataUrl,
                        "data": $.extend(this.getSearchData(), this.extraParams)
                    },
                    createdRow: function (row, data, dataIndex) {
                        //将事件发布出去
                        jQuery(_self).trigger("onTableCreatedRow", [row, data, dataIndex]);
                    },
                    fnInitComplete: function (settings, json) {
                        //将事件发布出去
                        jQuery(_self).trigger("onTableInitComplete", [settings, json]);
                    },
                    fnDrawCallback: function (settings) {
                        //重绘时初始化选择状态
                        if (_self.openChoose) {
                            let scrollBodyFindCondition = '.dataTables_scroll';
                            let fixedDivHeadFindCondition = '.DTFC_LeftHeadWrapper';
                            let fixedAllCheckFindCondition = fixedDivHeadFindCondition + ' #allCheck';
                            let selectBoxLength = $(scrollBodyFindCondition + ' [name="selectBox"]:not(:disabled)').length;
                            let checkBoxLength = $(scrollBodyFindCondition + ' [name="selectBox"]:not(:disabled):checked').length;
                            // 判断复选框是否全部被选中，进而判断全选是否选中
                            if (selectBoxLength !== 0 && selectBoxLength === checkBoxLength) {
                                $('#allCheck').prop("checked", 'checked');
                                $(fixedAllCheckFindCondition).prop("checked", 'checked');
                            } else {
                                $('#allCheck').prop("checked", false);
                                $(fixedAllCheckFindCondition).prop("checked", false);
                            }
                        }
                        //将事件发布出去
                        jQuery(_self).trigger("onTableDrawCallback", [settings]);
                    }
                };
                let tableOption = this.dataTableOption ? $.extend(defaultDataTableOption, this.dataTableOption) : defaultDataTableOption;
                this.dataTable = tableTag.DataTable(tableOption);
                // 绑定单选全选事件
                _self._bindCheckEvent(tableTag);
                // 修复固定列时的绑定问题(固定列会新创建一个table遮盖绑定事件)
                if (tableOption.fixedColumns) {
                    _self._fixedColumnsCheckEvent(tableTag);
                }
            }
        },
        //将文字转化可换行的div
        _textConversionWrapDiv: function (text, width) {
            text = text || '';
            width = width || '150';
            return '<div style="width:' + width + 'px;white-space:normal;line-height:1.3;word-wrap:break-word;display: inline-block;padding: 5px 0">' + text + '</div>';
        },
        //将文字切断成div
        _textBreakDiv: function (text, width) {
            text = text || '';
            width = width || 10;
            return "<div title='" + text + "'>" + (text && text.length > width ? text.substring(0, width) + '...' : text) + "</div>"
        },
        _buildActionButton() {
            if (!this.actionOption || !ComUtils.isArray(this.actionOption)) {
                return null;
            }
            let buildDiv = $.e('div.am-u-sm-12.am-u-md-6.am-u-lg-2>div.am-form-group>div.am-btn-toolbar>div.am-btn-group.am-btn-group-xs')
                .find('.am-btn-group');
            this.actionOption.some(function (item) {
                if (!item.action || (ComUtils.isNotFunction(item.action) && ComUtils.isNotString(item.action))) {
                    return false;
                }
                if (ComUtils.isString(item.action)) {
                    buildDiv.append('<a type="button" class="am-btn am-btn-success" href="' + item.action + '>' +
                        '<span class="' + item.icon + '"></span> ' + item.name + '  </a>');
                } else {
                    let aTag = $('<a type="button" class="am-btn am-btn-success" >' +
                        '<span class="' + item.icon + '"></span> ' + item.name + '  </a>');
                    aTag.click(function () {
                        item.action();
                    });
                    buildDiv.append(aTag);
                }
            });
            return buildDiv.parents('.am-u-sm-12');
        },
        //绑定全选单选事件
        _bindCheckEvent: function (tableTag) {
            if (!this.openChoose) return;
            let allCheckFindCondition = 'input[name="allCheck"]';
            let enableSelectSelector = 'input[name="selectBox"]:not(:disabled)';
            $(document).on('click', allCheckFindCondition, function (e) {
                //阻止事件冒泡到tr
                e.stopPropagation();
                if ($(this).is(':checked')) {
                    $(enableSelectSelector).prop('checked', true);
                } else {
                    $(enableSelectSelector).prop('checked', false);
                }
            });
            // 设置表格内滚动条时 data table会剥离头部至另一个div 因此写表头事件需要修改绑定对象
            $(document).on('click', 'thead tr', function (e) {
                let allCheck = $(allCheckFindCondition);
                // 先取反选择状态
                allCheck.prop('checked', !allCheck.is(':checked'));
                // $(this).toggleClass('selected');
                if (allCheck.is(':checked')) {
                    $(enableSelectSelector).prop('checked', true);
                } else {
                    $(enableSelectSelector).prop('checked', false);
                }
            });
            tableTag.on('click', 'tbody tr', function (e) {
                let thisCheckBox = $(this).find(enableSelectSelector);
                let leftCheckBox = $('.DTFC_Cloned tbody tr').eq($(this).index()).find(enableSelectSelector);

                // 先取反选择状态
                thisCheckBox.prop('checked', !thisCheckBox.is(':checked'));
                // 同时取反固定列时额外复框的选择状态
                leftCheckBox.prop('checked', !leftCheckBox.is(':checked'));
                // $(this).toggleClass('selected');
                if (thisCheckBox.length !== 0) {
                    if (thisCheckBox.is(':checked')) {
                        let selectBoxLength = $(enableSelectSelector).length;
                        let checkBoxLength = $(enableSelectSelector + ':checked').length;
                        // 判断复选框是否全部被选中，进而判断全选是否选中
                        if (selectBoxLength === checkBoxLength) {
                            $(allCheckFindCondition).prop("checked", 'checked');
                        }
                    } else {
                        if ($(allCheckFindCondition).is(":checked")) {
                            $(allCheckFindCondition).prop("checked", false);
                        }
                    }
                }
            });
            tableTag.on('click', 'tbody ' + enableSelectSelector, function (e) {
                //阻止事件冒泡到tr
                e.stopPropagation();
                if ($(this).is(':checked')) {
                    let selectBoxLength = $(enableSelectSelector).length;
                    let checkBoxLength = $(enableSelectSelector + ':checked').length;
                    // 判断复选框是否全部被选中，进而判断全选是否选中
                    if (selectBoxLength === checkBoxLength) {
                        $(allCheckFindCondition).prop("checked", 'checked');
                    }
                } else {
                    if ($(allCheckFindCondition).is(":checked")) {
                        $(allCheckFindCondition).prop("checked", false);
                    }
                }
            });
        },
        _fixedColumnsCheckEvent: function (tableTag) {
            if (!this.openChoose) return;
            let realAllCheck = tableTag.parent().prev().find('#allCheck');
            let fixedDivHeadFindCondition = '.DTFC_LeftHeadWrapper';
            let fixedDivBodyFindCondition = '.DTFC_LeftBodyWrapper';
            let fixedAllCheckFindCondition = fixedDivHeadFindCondition + ' #allCheck';
            let notDisabledSelectBtnFindCondition = 'input[name="selectBox"]:not(:disabled)';
            let scrollBodyFindCondition = '.dataTables_scroll';
            // 左侧固定后实际展示的复选框在 .DTFC_LeftWrapper下
            realAllCheck.click(function (e) {
                //阻止事件冒泡到tr
                e.stopPropagation();
                if ($(this).is(':checked')) {
                    $(notDisabledSelectBtnFindCondition).prop('checked', true);
                } else {
                    $(notDisabledSelectBtnFindCondition).prop('checked', false);
                }
            });
            // 设置表格内滚动条时 data table会剥离头部至另一个div 因此写表头事件需要修改绑定对象
            tableTag.parent().prev().on('click', 'thead tr', function (e) {
                let allCheck = $(this).find('#allCheck');
                // 触发按钮点击事件
                allCheck.click();
            });
            $(document).on('click', scrollBodyFindCondition + ' tbody tr', function (e) {
                e.stopPropagation();
                let thisCheckBox = $(this).find(notDisabledSelectBtnFindCondition);
                // 触发按钮点击事件
                thisCheckBox.click();
            });
            $(document).on('click', scrollBodyFindCondition + ' tbody ' + notDisabledSelectBtnFindCondition, function (e) {
                //阻止事件冒泡到tr
                e.stopPropagation();
                if ($(this).is(':checked')) {
                    let selectBoxLength = tableTag.find(notDisabledSelectBtnFindCondition).length;
                    let checkBoxLength = tableTag.find(notDisabledSelectBtnFindCondition + ':checked').length;
                    // 判断复选框是否全部被选中，进而判断全选是否选中
                    if (selectBoxLength === checkBoxLength) {
                        realAllCheck.prop("checked", 'checked');
                    }
                } else {
                    if (realAllCheck.is(":checked")) {
                        realAllCheck.prop("checked", false);
                    }
                }
                realAllCheck.change();
            });
            $(document).on('change', scrollBodyFindCondition + ' tbody ' + notDisabledSelectBtnFindCondition, function (e) {
                // 获取行索引
                let index = $(this).parents('tr').index() + 1;
                $(fixedDivBodyFindCondition + ' tr:eq(' + index + ')').find(notDisabledSelectBtnFindCondition).prop('checked', $(this).is(':checked'));
            });
            realAllCheck.change(function (e) {
                $(fixedAllCheckFindCondition).prop('checked', $(this).is(':checked'));
            });
            $(document).on('click', fixedAllCheckFindCondition, function () {
                realAllCheck.click();
            });
            $(document).on('click', fixedDivBodyFindCondition + ' tbody tr', function (e) {
                e.stopPropagation();
                // 获取行索引
                let index = $(this).index();
                tableTag.find('tbody').find(' tr:eq(' + index + ')').find(notDisabledSelectBtnFindCondition).click();
            });
        }
    };

    window.TableServer = TableServer;
})(window, jQuery);