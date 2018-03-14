import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by zht on 17/5/3.
 */
public class GenerateValueFiles {

    //标准宽度（一般根据UI的设计图定）
    private int baseW;
    //标准高度（一般根据UI的设计图定）
    private int baseH;
    //文件生成的地方
    private String dirStr = "./dimen/src/main/res";
    //定义文件内容
    private final static String WTemplate = "<dimen name=\"x{0}\">{1}px</dimen>\n";
    private final static String HTemplate = "<dimen name=\"y{0}\">{1}px</dimen>\n";

    /**
     * 生成文件夹的命名规则
     */
    private final static String VALUE_TEMPLATE = "values-{0}x{1}";
    //常见尺寸集
    private static final String SUPPORT_DIMESION = "300,400;320,480;480,800;480,854;540,888;540,960;600,1024;720,1184;720,1196;720,1280;768,1024;800,1280;1080,1812;1080,1920;1440,2560;";
    //这里是我自己写的，将values-hdpi等对应的尺寸对应到相对的文件中
    private String[] fileReplace = {"values-hdpi", "values-mdpi", "values-xhdpi", "values-xxhdpi", "values-xxxhdpi"};
    private int[] fileReplacePosition = {800, 480, 480, 320, 1280, 720, 1920, 1080, 2560, 1440};

    private String supportStr = SUPPORT_DIMESION;

    public GenerateValueFiles(int baseX, int baseY, String supportStr) {
        this.baseW = baseX;
        this.baseH = baseY;

        if (!this.supportStr.contains(baseX + "," + baseY)) {
            this.supportStr += baseX + "," + baseY + ";";
        }

        this.supportStr += validateInput(supportStr);

        System.out.println(supportStr);

        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdir();

        }
        System.out.println(dir.getAbsoluteFile());

    }

    /**
     * @param supportStr w,h_...w,h;
     * @return
     */
    private String validateInput(String supportStr) {
        StringBuffer sb = new StringBuffer();
        String[] vals = supportStr.split("_");
        int w = -1;
        int h = -1;
        String[] wh;
        for (String val : vals) {
            try {
                if (val == null || val.trim().length() == 0)
                    continue;

                wh = val.split(",");
                w = Integer.parseInt(wh[0]);
                h = Integer.parseInt(wh[1]);
            } catch (Exception e) {
                System.out.println("skip invalidate params : w,h = " + val);
                continue;
            }
            sb.append(w + "," + h + ";");
        }

        return sb.toString();
    }

    /**
     * 循环生成XML
     */
    public void generate() {
        String[] vals = supportStr.split(";");
        for (String val : vals) {
            String[] wh = val.split(",");
            generateXmlFile(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
        }

    }

    /**
     * 根据屏幕的尺寸生成 baseW*baseH 对应的xml文件
     *
     * @param w 屏幕的宽度
     * @param h 屏幕的高度
     */
    private void generateXmlFile(int w, int h) {
        //生成宽度的XML
        StringBuffer sbForWidth = new StringBuffer();
        //生成头标签
        sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

        sbForWidth.append("<resources>");
        float cellw = w * 1.0f / baseW;

        System.out.println("width : " + w + "," + baseW + "," + cellw);

        for (int i = 1; i < baseW; i++) {
            sbForWidth.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sbForWidth.append(WTemplate.replace("{0}", baseW + "").replace("{1}",
                w + ""));
        sbForWidth.append("</resources>");

        //生成高度的XML
        StringBuffer sbForHeight = new StringBuffer();
        sbForHeight.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForHeight.append("<resources>");
        float cellh = h * 1.0f / baseH;
        System.out.println("height : " + h + "," + baseH + "," + cellh);
        for (int i = 1; i < baseH; i++) {
            sbForHeight.append(HTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }
        sbForHeight.append(HTemplate.replace("{0}", baseH + "").replace("{1}",
                h + ""));
        sbForHeight.append("</resources>");
        /**
         * values-hdpi      ---> 800x480
         * values-mdpi      ---> 480x320
         * values-xhdpi     ---> 1280x720
         * values-xxhdpi    ---> 1920x1080
         * values-xxxhdpi   ---> 2560x1440
         */
        //生成文件夹
        File fileDir = null;
        //h 高  w 宽
        //根据尺寸选择文件夹的命名
        int position = -1;
        for (int i = 0; i < fileReplace.length; i++) {
            if (h == fileReplacePosition[2 * i] && w == fileReplacePosition[2 * i + 1]) {
                position = i;
            }
        }
        if (position != -1) {
            fileDir = new File(dirStr + File.separator
                    + fileReplace[position]);

        } else {
            fileDir = new File(dirStr + File.separator
                    + VALUE_TEMPLATE.replace("{0}", h + "")//
                    .replace("{1}", w + ""));
        }
        fileDir.mkdir();

        File layxFile = new File(fileDir.getAbsolutePath(), "lay_x.xml");
        File layyFile = new File(fileDir.getAbsolutePath(), "lay_y.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sbForWidth.toString());
            pw.close();
            pw = new PrintWriter(new FileOutputStream(layyFile));
            pw.print(sbForHeight.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

    /**
     * 主函数的位置
     *
     * @param args
     */
    public static void main(String[] args) {
        int baseW = 750;
        int baseH = 1334;
        String addition = "";
        try {
            if (args.length >= 3) {
                baseW = Integer.parseInt(args[0]);
                baseH = Integer.parseInt(args[1]);
                addition = args[2];
            } else if (args.length >= 2) {
                baseW = Integer.parseInt(args[0]);
                baseH = Integer.parseInt(args[1]);
            } else if (args.length >= 1) {
                addition = args[0];
            }
        } catch (NumberFormatException e) {

            System.err
                    .println("right input params : java -jar xxx.jar width height w,h_w,h_..._w,h;");
            e.printStackTrace();
            System.exit(-1);
        }

        new GenerateValueFiles(baseW, baseH, addition).generate();
    }

}
