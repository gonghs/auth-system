/**
 * 时间工具
 *
 * @author maple
 * @date 2019-10-30
 */

(function (window) {
        let mouthDateCount = [0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334];
        let numString = "一二三四五六七八九十";
        let monString = "正二三四五六七八九十冬腊";
        let cYear, cMonth, cDay, currentDate;
        let calendarData = [0xA4B, 0x5164B, 0x6A5, 0x6D4, 0x415B5, 0x2B6, 0x957, 0x2092F, 0x497, 0x60C96, 0xD4A, 0xEA5, 0x50DA9, 0x5AD, 0x2B6, 0x3126E, 0x92E, 0x7192D, 0xC95, 0xD4A, 0x61B4A, 0xB55, 0x56A, 0x4155B, 0x25D, 0x92D, 0x2192B, 0xA95, 0x71695, 0x6CA, 0xB55, 0x50AB5, 0x4DA, 0xA5B, 0x30A57, 0x52B, 0x8152A, 0xE95, 0x6AA, 0x615AA, 0xAB5, 0x4B6, 0x414AE, 0xA57, 0x526, 0x31D26, 0xD95, 0x70B55, 0x56A, 0x96D, 0x5095D, 0x4AD, 0xA4D, 0x41A4D, 0xD25, 0x81AA5, 0xB54, 0xB6A, 0x612DA, 0x95B, 0x49B, 0x41497, 0xA4B, 0xA164B, 0x6A5, 0x6D4, 0x615B4, 0xAB6, 0x957, 0x5092F, 0x497, 0x64B, 0x30D4A, 0xEA5, 0x80D65, 0x5AC, 0xAB6, 0x5126D, 0x92E, 0xC96, 0x41A95, 0xD4A, 0xDA5, 0x20B55, 0x56A, 0x7155B, 0x25D, 0x92D, 0x5192B, 0xA95, 0xB4A, 0x416AA, 0xAD5, 0x90AB5, 0x4BA, 0xA5B, 0x60A57, 0x52B, 0xA93, 0x40E95];

        /**
         * 获取时间和周信息
         *
         * @returns {string} 中文描述
         */
        window.getCurrentDateAndWeekDay = function () {
            let d = new Date();
            let year = d.getFullYear();
            let month = d.getMonth() + 1;
            let date = d.getDate();
            let week = d.getDay();
            let curDateTime = year;
            if (month > 9)
                curDateTime = curDateTime + "年" + month;
            else
                curDateTime = curDateTime + "年0" + month;
            if (date > 9)
                curDateTime = curDateTime + "月" + date + "日";
            else
                curDateTime = curDateTime + "月0" + date + "日";
            let weekday = "";
            switch (week) {
                case 0:
                    weekday = "星期日";
                    break;
                case 1:
                    weekday = "星期一";
                    break;
                case 2:
                    weekday = "星期二";
                    break;
                case 3:
                    weekday = "星期三";
                    break;
                case 4:
                    weekday = "星期四";
                    break;
                case 5:
                    weekday = "星期五";
                    break;
                default:
                    weekday = "星期六";
            }
            curDateTime = curDateTime + " " + weekday;
            return curDateTime;
        };


        /**
         * 获取农历时间
         *
         * @returns {string}
         */
        window.getCalDate = function () {
            let D = new Date();
            let yy = D.getFullYear();
            let mm = D.getMonth() + 1;
            let dd = D.getDate();
            if (yy < 100) yy = "19" + yy;
            return getLunarDay(yy, mm, dd);
        };

        /**
         * 获取农历月,日
         *
         * @return {string} 字符串
         */
        function getLunarDay(solarYear, solarMonth, solarDay) {
            if (solarYear < 1921 || solarYear > 2020) {
                return "";
            } else {
                solarMonth = (parseInt(solarMonth) > 0) ? (solarMonth - 1) : 11;
                e2c(solarYear, solarMonth, solarDay);
                return getCurrentDateString();
            }
        }

        function getCurrentDateString() {
            let tmp = "";
            if (cMonth < 1) {
                tmp += "(闰)";
                tmp += monString.charAt(-cMonth - 1);
            } else {
                tmp += monString.charAt(cMonth - 1);
            }
            tmp += "月";
            tmp += (cDay < 11) ? "初" : ((cDay < 20) ? "十" : ((cDay < 30) ? "廿" : "三十"));
            if (cDay % 10 !== 0 || cDay === 10) {
                tmp += numString.charAt((cDay - 1) % 10);
            }
            return tmp;
        }

        /**
         * 新历转农历
         */
        function e2c() {
            currentDate = (arguments.length !== 3) ? new Date() : new Date(arguments[0], arguments[1], arguments[2]);
            let total, m, n, k;
            let isEnd = false;
            let tmp = currentDate.getYear();
            if (tmp < 1900) {
                tmp += 1900;
            }
            total = (tmp - 1921) * 365 + Math.floor((tmp - 1921) / 4) + mouthDateCount[currentDate.getMonth()] + currentDate.getDate() - 38;

            if (currentDate.getYear() % 4 === 0 && currentDate.getMonth() > 1) {
                total++;
            }
            for (m = 0; ; m++) {
                k = (calendarData[m] < 0xfff) ? 11 : 12;
                for (n = k; n >= 0; n--) {
                    if (total <= 29 + getBit(calendarData[m], n)) {
                        isEnd = true;
                        break;
                    }
                    total = total - 29 - getBit(calendarData[m], n);
                }
                if (isEnd) break;
            }
            cYear = 1921 + m;
            cMonth = k - n + 1;
            cDay = total;
            if (k === 12) {
                if (cMonth === Math.floor(calendarData[m] / 0x10000) + 1) {
                    cMonth = 1 - cMonth;
                }
                if (cMonth > Math.floor(calendarData[m] / 0x10000) + 1) {
                    cMonth--;
                }
            }
        }

        /**
         * 获取指定二进制位值
         *
         * @return {number}
         */
        function getBit(m, n) {
            return (m >> n) & 1;
        }
    }
)(window);

