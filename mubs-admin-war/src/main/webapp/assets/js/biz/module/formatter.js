    function formatExpertState(cellvalue, options, rowObject){
        if("1" == cellvalue){
            return "隐藏";
        } else if("2" == cellvalue){
            return "显示";
        } else { //3
            return "冻结";
        }
    }

    function formatExpertType(cellvalue, options, rowObject){
        if("2000" == cellvalue){
            return "习悦";
        } else if("2001" == cellvalue){
            return "全通";
        }
    }