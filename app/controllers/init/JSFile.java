package controllers.init;

import play.Play;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


public class JSFile {

    private static class _ExtensionFilter implements FilenameFilter{
        String ext;
        _ExtensionFilter(String ext) {
            this.ext=ext;
        }

        @Override
        public boolean accept(File file, String filename) {
            return filename.endsWith(ext);
        }
    }

    public static FilenameFilter FILTER_ALL = new _ExtensionFilter("");

    public static FilenameFilter FILTER_JS = new _ExtensionFilter("js");

    public static FilenameFilter FILTER_HTML = new _ExtensionFilter("html");

    public static String get(String path) {
        return get(Play.getFile(path),path, FILTER_JS);
    }

    private static String script(String path) {
        return "<script type=\"text/javascript\" src=\""+path+"\"></script>\n";
    }

    public static String get(File path, String baseUrl,FilenameFilter filter) {
        String scripts="";
        if (path.isFile()) {
            if (filter.accept(path, path.getName())) {
                scripts += script(baseUrl);
            }
        }else{
            File[] files = path.listFiles();
            if(files==null)
                return "";
            for (File file : files) {
                scripts += get(file, baseUrl + "/"+ file.getName(), filter);
            }

        }
        return scripts;
    }





//    public static String get(String path,FilenameFilter filter) {
//        return get(Play.getFile(path), path,filter);
//    }
//
//    public static String get(String path) {
//        return get(path, FILTER_ALL);
//    }
//
//    public static String getjs(String path) {
//        return get(path, FILTER_JS);
//    }
//
//    public static String gethtml(String path) {
//        return get(path, FILTER_HTML);
//    }
//
//    public static String get(File file,String path,FilenameFilter filter)  {
//        String s = "";
//        File[] files = file.listFiles();
//        if(files==null)
//            return "";
//        for (File f : files) {
//            if (f.isFile()) {
//                if (filter.accept(f, f.getName())) {
//                    if (s != "") {
//                        s += ",";
//                    }
//                    s += "\""+ path + "/" + f.getName()+"\"";
//                }
//            }else {
//                if (s != "") {
//                    s += ",";
//                }
//                s += get(new File(f.getAbsolutePath()), path + "/" + f.getName(), filter);
//            }
//        }
//        return s;
//    }
//
//    public static List<File> getList(File file,String path,FilenameFilter filter) {
//        List<File> fileList = new ArrayList<File>();
//        File[] files = file.listFiles();
//        if (files == null)
//            return fileList;
//        for (File f : files) {
//            if (f.isFile()) {
//                if (filter.accept(f, f.getName())) {
//                    fileList.add(f);
//                }
//            } else {
//                fileList.addAll(getList(new File(f.getAbsolutePath()), path + "/" + f.getName(), filter));
//            }
//        }
//        return fileList;
//    }
//
//    public static String js(){
//        String js = "";
//        String statejs = JSFile.getjs("/public/states");
//        js+=statejs;
////
////        String servicejs = JSFile.getjs("/public/services");
////        if (js != "") {
////            js += ",";
////        }
////        js+=servicejs;
//        return js;
//    }
}
