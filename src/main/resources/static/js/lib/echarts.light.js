(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['exports', 'echarts'], factory);
    } else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
        // CommonJS
        factory(exports, require('echarts'));
    } else {
        // Browser globals
        factory({}, root.echarts);
    }
}(this, function (exports, echarts) {
    var log = function (msg) {
        if (typeof console !== 'undefined') {
            console && console.error && console.error(msg);
        }
    };
    if (!echarts) {
        log('ECharts is not Loaded');
        return;
    }
    echarts.registerTheme('echarts.light', {
        "color": [
            "#673ab7",
            "#009688",
            "#ff5722",
            "#ffc107",
            "#03a9f4",
            "#4caf50",
            "#e91e63",
            "#607d8b"
        ],
        "backgroundColor": "rgba(0,0,0,0)",
        "textStyle": {},
        "title": {
            "textStyle": {
                "color": "rgba(0,0,0,0.8)"
            },
            "subtextStyle": {
                "color": "rgba(0,0,0,0.4)"
            }
        },
        "line": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "2"
                }
            },
            "lineStyle": {
                "normal": {
                    "width": 2
                }
            },
            "symbolSize": "8",
            "symbol": "circle",
            "smooth": false
        },
        "radar": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "2"
                }
            },
            "lineStyle": {
                "normal": {
                    "width": 2
                }
            },
            "symbolSize": "8",
            "symbol": "circle",
            "smooth": false
        },
        "bar": {
            "itemStyle": {
                "normal": {
                    "barBorderWidth": "0",
                    "barBorderColor": "#ccc"
                },
                "emphasis": {
                    "barBorderWidth": "0",
                    "barBorderColor": "#ccc"
                }
            }
        },
        "pie": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                }
            }
        },
        "scatter": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                }
            }
        },
        "boxplot": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                }
            }
        },
        "parallel": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                }
            }
        },
        "sankey": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                }
            }
        },
        "funnel": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                }
            }
        },
        "gauge": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                },
                "emphasis": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                }
            }
        },
        "candlestick": {
            "itemStyle": {
                "normal": {
                    "color": "#ff5722",
                    "color0": "#4caf50",
                    "borderColor": "#ff5722",
                    "borderColor0": "#4caf50",
                    "borderWidth": 1
                }
            }
        },
        "graph": {
            "itemStyle": {
                "normal": {
                    "borderWidth": "0",
                    "borderColor": "#ccc"
                }
            },
            "lineStyle": {
                "normal": {
                    "width": 1,
                    "color": "#aaaaaa"
                }
            },
            "symbolSize": "8",
            "symbol": "circle",
            "smooth": false,
            "color": [
                "#673ab7",
                "#009688",
                "#ff5722",
                "#ffc107",
                "#03a9f4",
                "#4caf50",
                "#e91e63",
                "#607d8b"
            ],
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        },
        "map": {
            "itemStyle": {
                "normal": {
                    "areaColor": "rgba(0,0,0,0.5)",
                    "borderColor": "#ffffff",
                    "borderWidth": "1"
                },
                "emphasis": {
                    "areaColor": "#ff5722",
                    "borderColor": "#ffffff",
                    "borderWidth": "1"
                }
            },
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#000000"
                    }
                },
                "emphasis": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        },
        "geo": {
            "itemStyle": {
                "normal": {
                    "areaColor": "rgba(0,0,0,0.5)",
                    "borderColor": "#ffffff",
                    "borderWidth": "1"
                },
                "emphasis": {
                    "areaColor": "#ff5722",
                    "borderColor": "#ffffff",
                    "borderWidth": "1"
                }
            },
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#000000"
                    }
                },
                "emphasis": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        },
        "categoryAxis": {
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "rgba(0,0,0,0.2)"
                }
            },
            "axisTick": {
                "show": true,
                "lineStyle": {
                    "color": "rgba(0,0,0,0.2)"
                }
            },
            "axisLabel": {
                "show": true,
                "textStyle": {
                    "color": "rgba(0,0,0,0.8)"
                }
            },
            "splitLine": {
                "show": true,
                "lineStyle": {
                    "color": [
                        "rgba(0,0,0,0)"
                    ]
                }
            },
            "splitArea": {
                "show": false,
                "areaStyle": {
                    "color": [
                        "rgba(250,250,250,0.3)",
                        "rgba(200,200,200,0.3)"
                    ]
                }
            }
        },
        "valueAxis": {
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "rgba(0,0,0,0.2)"
                }
            },
            "axisTick": {
                "show": true,
                "lineStyle": {
                    "color": "rgba(0,0,0,0.2)"
                }
            },
            "axisLabel": {
                "show": true,
                "textStyle": {
                    "color": "rgba(0,0,0,0.6)"
                }
            },
            "splitLine": {
                "show": true,
                "lineStyle": {
                    "color": [
                        "rgba(0,0,0,0.1)"
                    ]
                }
            },
            "splitArea": {
                "show": false,
                "areaStyle": {
                    "color": [
                        "rgba(250,250,250,0.3)",
                        "rgba(200,200,200,0.3)"
                    ]
                }
            }
        },
        "logAxis": {
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "rgba(0,0,0,0.2)"
                }
            },
            "axisTick": {
                "show": true,
                "lineStyle": {
                    "color": "rgba(0,0,0,0.2)"
                }
            },
            "axisLabel": {
                "show": true,
                "textStyle": {
                    "color": "rgba(0,0,0,0.8)"
                }
            },
            "splitLine": {
                "show": true,
                "lineStyle": {
                    "color": [
                        "rgba(0,0,0,0.2)"
                    ]
                }
            },
            "splitArea": {
                "show": false,
                "areaStyle": {
                    "color": [
                        "rgba(250,250,250,0.3)",
                        "rgba(200,200,200,0.3)"
                    ]
                }
            }
        },
        "timeAxis": {
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "#333"
                }
            },
            "axisTick": {
                "show": true,
                "lineStyle": {
                    "color": "#333"
                }
            },
            "axisLabel": {
                "show": true,
                "textStyle": {
                    "color": "#333"
                }
            },
            "splitLine": {
                "show": true,
                "lineStyle": {
                    "color": [
                        "#ccc"
                    ]
                }
            },
            "splitArea": {
                "show": false,
                "areaStyle": {
                    "color": [
                        "rgba(250,250,250,0.3)",
                        "rgba(200,200,200,0.3)"
                    ]
                }
            }
        },
        "toolbox": {
            "iconStyle": {
                "normal": {
                    "borderColor": "rgba(0,0,0,0.4)"
                },
                "emphasis": {
                    "borderColor": "rgba(0,0,0,0.8)"
                }
            }
        },
        "legend": {
            "textStyle": {
                "color": "rgba(0,0,0,0.6)"
            }
        },
        "tooltip": {
            "axisPointer": {
                "lineStyle": {
                    "color": "rgba(0,0,0,0.5)",
                    "width": 1
                },
                "crossStyle": {
                    "color": "rgba(0,0,0,0.5)",
                    "width": 1
                }
            }
        },
        "timeline": {
            "lineStyle": {
                "color": "#005eaa",
                "width": 1
            },
            "itemStyle": {
                "normal": {
                    "color": "#005eaa",
                    "borderWidth": 1
                },
                "emphasis": {
                    "color": "#005eaa"
                }
            },
            "controlStyle": {
                "normal": {
                    "color": "#005eaa",
                    "borderColor": "#005eaa",
                    "borderWidth": 0.5
                },
                "emphasis": {
                    "color": "#005eaa",
                    "borderColor": "#005eaa",
                    "borderWidth": 0.5
                }
            },
            "checkpointStyle": {
                "color": "#005eaa",
                "borderColor": "rgba(49,107,194,0.5)"
            },
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#005eaa"
                    }
                },
                "emphasis": {
                    "textStyle": {
                        "color": "#005eaa"
                    }
                }
            }
        },
        "visualMap": {
            "color": [
                "#1790cf",
                "#a2d4e6"
            ]
        },
        "dataZoom": {
            "backgroundColor": "rgba(47,69,84,0)",
            "dataBackgroundColor": "rgba(47,69,84,0.3)",
            "fillerColor": "rgba(167,183,204,0.4)",
            "handleColor": "#a7b7cc",
            "handleSize": "100%",
            "textStyle": {
                "color": "#333333"
            }
        },
        "markPoint": {
            "label": {
                "normal": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                },
                "emphasis": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        }
    });
}));