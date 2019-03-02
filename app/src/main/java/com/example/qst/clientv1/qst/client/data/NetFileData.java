package com.example.qst.clientv1.qst.client.data;

public class NetFileData {
    private long fileSize = 0;// 文件长度应该long型数据，否则大于2GB的文件大小无法表达
    private String fileName = "$error";// 文件名称，不含目录信息,默认值用于表示文件出错
    private String filePath = ".\\";// 该文件对象所处的目录，默认值为当前相对目录
    private String fileSizeStr = "0";// 文件的大小，用字符串表示，能智能地选择B、KB、MB、GB来表达
    private String fileModifiedDate = "1970-01-01 00:00:00";// 文件最近修改日期，默认值为1970年基准时间
    private int fileType = 0;// fileType=0为文件，fileType=1为普通文件夹，fileType=2为盘符,fileType=3为"..."或".."
    public NetFileData(String fileInfo, String filePath){
        String [] info=fileInfo.split(">");
        this.fileName=info[0];
        this.fileModifiedDate=info[1];
        this.fileSize=Long.parseLong(info[2]);
        this.fileType=Integer.parseInt(info[3]);
        this.filePath=filePath;
        dealSize();
    }
    //构造两个三个点返回盘符
    public NetFileData() {
        this.fileName="...";
        this.fileType=3;
    }
    //构造两个点返回上一级
    public NetFileData(String filePath) {
        this.fileName="..";
        this.fileType=3;
        this.filePath=filePath+"..";
    }

    private void dealSize()
    {
        if(fileType==0)
        {
            if(fileSize<1024)
            {
                fileSizeStr=fileSize+"B";
            }
            else if(fileSize/1024<1024)
            {
                fileSizeStr=(double)(fileSize * 100 /1024)/100+"KB";
            }
            else if(fileSize/1024/1024<1024)
            {
                fileSizeStr=(double)(fileSize * 100 /1024/1024)/100+"MB";
            }
            else if(fileSize/1024/1024/1024<1024)
            {
                fileSizeStr=(double)(fileSize * 100 /1024/1024/1024)/100+"GB";
            }
        }
        else
            fileSizeStr="";
    }
    public long getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileSizeStr() {
        return fileSizeStr;
    }

    public String getFileModifiedDate() {
        return fileModifiedDate;
    }

    public int getFileType() {
        return fileType;
    }
}
