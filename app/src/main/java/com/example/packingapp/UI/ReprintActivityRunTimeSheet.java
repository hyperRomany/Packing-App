package com.example.packingapp.UI;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packingapp.Database.AppDatabase;
import com.example.packingapp.R;
import com.example.packingapp.databinding.ActivityReprintRunTimeSheetBinding;
import com.example.packingapp.model.TimeSheet.RecordsHeader;
import com.example.packingapp.model.TimeSheet.RecordsItem;
import com.example.packingapp.model.TimeSheet.Response;
import com.example.packingapp.viewmodel.ReprintRunTimeSheetViewModel;
import com.onbarcode.barcode.android.AndroidColor;
import com.onbarcode.barcode.android.AndroidFont;
import com.onbarcode.barcode.android.Code93;
import com.onbarcode.barcode.android.IBarcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ReprintActivityRunTimeSheet extends AppCompatActivity {
    private static final String TAG = "ReprintActivityRunTimeS";
    ReprintRunTimeSheetViewModel reprintRunTimeSheetViewModel;
    ActivityReprintRunTimeSheetBinding binding;
    AppDatabase database;
    List<String> Response_id_for_runtimesheet_Orders;
    List<RecordsItem> Response_Recordsitems_list_for_runtimesheet_Orders;
    List<RecordsHeader> Response_RecordsHeader_list_for_runtimesheet_Orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReprintRunTimeSheetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Response_id_for_runtimesheet_Orders=new ArrayList<>();
        Response_Recordsitems_list_for_runtimesheet_Orders=new ArrayList<>();
        Response_RecordsHeader_list_for_runtimesheet_Orders =new ArrayList<>();

        reprintRunTimeSheetViewModel= ViewModelProviders.of(this).get(ReprintRunTimeSheetViewModel.class);
        database= AppDatabase.getDatabaseInstance(this);
        binding.editRunsheetnumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent == null
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER) {
                    ReprintFUN();
                }
                return false;
            }
        });

        binding.btnPrintAwb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReprintFUN();
            }
        });


        reprintRunTimeSheetViewModel.getSheetLiveData().observe(ReprintActivityRunTimeSheet.this, new Observer<Response>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(Response response) {
                if (response.getRecords().size() > 0) {
                    Log.e("response",response.toString());

                    Response_id_for_runtimesheet_Orders.add(response.getId());
                    Response_Recordsitems_list_for_runtimesheet_Orders.addAll(response.getRecords());
                    Response_RecordsHeader_list_for_runtimesheet_Orders.addAll(response.getRecordsHeader());
                    Log.e(TAG, "onChanged:response.getId() " + response.getId());

                    PrintRunTimeSheet(binding.editRunsheetnumber.getText().toString(), response.getRecords());
                } else {
                    Toast.makeText(ReprintActivityRunTimeSheet.this, R.string.nodata, Toast.LENGTH_SHORT).show();

                }

            }
        });

        reprintRunTimeSheetViewModel.mutableLiveDataError_SheetData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "onChanged: "+s );

                if (s.equals("HTTP 503 Service Unavailable")) {
                    Toast.makeText(ReprintActivityRunTimeSheet.this, getResources().getString(R.string.order_not_found), Toast.LENGTH_SHORT).show();
//               Constant.ToastDialoge(getResources().getString(R.string.order_not_found) , AssignPackedOrderForZoneAndDriverActivity.this);
                }else {
                    Toast.makeText(ReprintActivityRunTimeSheet.this, s, Toast.LENGTH_LONG).show();
//                    Constant.ToastDialoge(s , AssignPackedOrderForZoneAndDriverActivity.this);

                }
            }
        });

    }
    public void ReprintFUN(){
        if (!binding.editRunsheetnumber.getText().toString().isEmpty() ) {

            reprintRunTimeSheetViewModel.SheetData(binding.editRunsheetnumber.getText().toString());

        }else {
                binding.editRunsheetnumber.setError(getString(R.string.enter));
                binding.editRunsheetnumber.requestFocus();

        }
    }

    private void OpenPdf_FUN() {
        //                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                browserIntent.setDataAndType(Uri.parse("/storage/emulated/0/HyperOne.pdf"), "application/pdf");
//                startActivity(browserIntent);

        // File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/HyperOne.pdf");
        //    Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("/storage/emulated/0/HyperOne.pdf"), "application/pdf");
        //         intent.setDataAndType(Uri.parse("/storage/emulated/0/HyperOne.pdf"), "*/*");
        //   intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //    startActivity(intent);

        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"HyperOne.pdf");//File path

        if (pdfFile.exists()) //Checking if the file exists or not
        {
            Uri path = Uri.fromFile(pdfFile);
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
//            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setDataAndType(Uri.parse("content:///storage/emulated/0/HyperOne.pdf"), "application/pdf");
            objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(objIntent);//Starting the pdf viewer
        } else {
            Toast.makeText(ReprintActivityRunTimeSheet.this, "The file not exists! ", Toast.LENGTH_SHORT).show();
        }

    }

    //TODO
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void PrintRunTimeSheet(String id , List<RecordsItem> items) {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        Log.e(TAG, "PrintRunTimeSheet:itemssize "+items.size() );
        Log.e(TAG, "PrintRunTimeSheet: id  "+id );
        createPdf(id , items);
        OpenPdf_FUN();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    // TODO this last version but with one page
    private void createPdf(String id , List<RecordsItem> items) {
        List<RecordsItem> data=items;
        ArrayList<String> outBounds=new ArrayList<>();
        for (int i =0;i<items.size();i++)
        {
            outBounds.add(items.get(i).getOUTBOUND_DELIVERY());
        }

        Float total = 0f;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        PdfDocument pdfDocument = new PdfDocument();
//        int noOfPages=data.size() /14 ;
//        Log.e(TAG, "createPdf:noOfPagesBefore / "+noOfPages);
//        Log.e(TAG, "datasize "+data.size());
//        if (noOfPages==0)
//        {
//            noOfPages=1;
//        }
//        Log.e(TAG, "createPdf:noOfPages "+data.size() %14 );
//
//        if (data.size() %14 > 0){
//            noOfPages=Integer.valueOf(noOfPages)+1;
//        }
        int noOfPages=items.size() /14 ;
        Log.e(TAG, "createPdf:items.size() "+items.size() );
        Log.e(TAG, "createPdf:noOfPagesBefore % "+noOfPages );
        if (items.size() %14 >0){
            noOfPages++;
        }
        Log.e(TAG, "createPdf:noOfPages "+noOfPages );

        for (int j=0;j < noOfPages;j++) {
            Paint paint = new Paint();
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setTextSize(30.0f);
            PdfDocument.Page page = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(3000, 2000, j+1).create());
            Canvas canvas = page.getCanvas();
            canvas.drawText("إقرار إستلام /Receiving Avowal" + " رقم  " + id, 1250.0f, 60.0f, paint);

            canvas.drawText("التاريخ/Date : " + currentDate + "           الوقت/Time : " + currentTime + " ",
                    1150.0f, 100.0f, paint);
            canvas.drawText("استلمت أنا ....................................... رقم قومي .............................  مندوب (شركة هايبروان للتجارة) البضاعة الموجودة بالشحنات المذكورأرقامها بالأسفل",
                    500.0f, 140.0f, paint);
            canvas.drawText("وذلك لتسليمها لعملاء الشركة وتحصيل قيمتها منهم على أن ألتزم برد الطلبيات التي لم تسلم للعملاء لمخزن الشركة بنفس حالة إستلامها وتسديد ما أقوم بتحصيله",
                    550.0f, 180.0f, paint);
            canvas.drawText("من العملاء لخزينة الشركة وتعتبر البضاعة وما أقوم بتحصيله من العملاء هو أمانة في ذمتي أتعهد بتسليمها للشركة, وإذا أخلللت بذلك أكون مبددا وخائنا للأمانة . ",
                    550.0f, 220.0f, paint);
            canvas.drawText("وأتحمل كافة المسئولية الجنائية والمدنية المترتبة على ذلك. ",
                    1000.0f, 260.0f, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2.0f);
            Paint paint2 = paint;


            //bottom potion of header liner
            canvas.drawRect(30.0f, 1800.0f, 2940.0f, 280.0f, paint2);
            paint.setTextAlign(Paint.Align.RIGHT);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawText("S/م", 2925.0f, 310.0f, paint);
            canvas.drawLine(2870.0f, 280.0f, 2870.0f, 1800.0f, paint2);
            canvas.drawText("outBound", 2852.0f, 310.0f, paint);
            canvas.drawLine(2650.0f, 280.0f, 2650.0f, 1800.0f, paint2);
            canvas.drawText("رقم الشحنة", 2400.0f, 310.0f, paint);
            canvas.drawLine(2100.0f, 280.0f, 2100.0f, 1800.0f, paint2);
            canvas.drawText("قيمة الشحنة", 2095.0f, 310.0f, paint);
            canvas.drawLine(1920.0f, 280.0f, 1920.0f, 1800.0f, paint2);
            canvas.drawText("طريقة الدفع", 1905.0f, 310.0f, paint);
            canvas.drawLine(1755.0f, 280.0f, 1755.0f, 1800.0f, paint2);
            canvas.drawText("نوع الشحنه", 1750.0f, 310.0f, paint);
            canvas.drawLine(1610.0f, 280.0f, 1610.0f, 1800.0f, paint2);
            canvas.drawText("إسم العميل", 1500.0f, 310.0f, paint);
            canvas.drawLine(1260.0f, 280.0f, 1260.0f, 1800.0f, paint2);
            canvas.drawText("تلفون العميل", 1160.0f, 310.0f, paint);
            canvas.drawLine(920.0f, 280.0f, 920.0f, 1800.0f, paint2);
            canvas.drawText("عنوان العميل", 740.0f, 310.0f, paint);

       /* canvas.drawLine(1260.0f, 280.0f, 1260.0f, 1800.0f, paint2);
        canvas.drawText("عنوان العميل", 1160.0f, 310.0f, paint);
        canvas.drawLine(900.0f, 280.0f, 900.0f, 1800.0f, paint2);
        canvas.drawText("تلفون العميل", 780.0f, 310.0f, paint);
*/

            canvas.drawLine(430.0f, 280.0f, 430.0f, 1800.0f, paint2);
            canvas.drawText("ملاحظات", 290.0f, 310.0f, paint);

            //bottom of header row  line
            canvas.drawLine(30.0f, 330.0f, 2940.0f, 330.0f, paint2);

            canvas.drawText("توقيع المستلم/Receiver sign", 2400.0f, 1850.0f, paint);
            canvas.drawText("توقيع مسئول أمن المخزن", 1700.0f, 1850.0f, paint);

            canvas.drawText("توقيع منسق التوصيل", 1000.0f, 1850.0f, paint);
            int pos = 0;


            Map<String, List<RecordsItem>> personByAge = new HashMap<>();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                personByAge = items.stream()
                        .collect(Collectors.groupingBy(RecordsItem::getOUTBOUND_DELIVERY));

            }
            ArrayList<String> list = removeDuplicates(outBounds);
            Log.e("out", list.toString());
            for (int x = 0; x < list.size(); x++) {
                Log.e("x",""+x);
                total = 0f;
                Log.e("out4", personByAge.get(list.get(x)).toString());
                Log.e("out4", ""+personByAge.get(list.get(x)).size());
                items.clear();
                items = personByAge.get(list.get(x));

                //Log.e("createPdf:noOfPages-1")
                Log.e(TAG, "createPdf:noOfPages-1 "+ ((noOfPages-1)*13) );
                Log.e(TAG, "createPdf:noOfPages-1 "+ ((noOfPages-1)*13) );
//            Log.e(TAG, "createPdf:noOfPages-1 "+ (j*17) );
//            for (int i = (j*17); i < items.size(); i++) {

                Log.e(TAG, "createPdf:X "+x );
                Log.e(TAG, "createPdf:personByAge "+ personByAge.size() );
                Log.e(TAG, "createPdf:list.get(x) "+ list.get(x) );

                int NoOfItemsForPages=(personByAge.get(list.get(x)).size())/13 ;
                Log.e(TAG, "createPdf:NoOfItemsForPages "+NoOfItemsForPages );
                Log.e(TAG, "createPdf:NoOfItemsForPages "+NoOfItemsForPages );
                if (NoOfItemsForPages %13 >0){
                    NoOfItemsForPages++;
                }
                Log.e(TAG, "createPdf:NoOfItemsForPages "+NoOfItemsForPages );

               // for (int k = 0; k < NoOfItemsForPages ; k++) {

                    for (int i = (j*14); i < (personByAge.get(list.get(x)).size()) ; i++) {
                        if (i == 14 && j==0) {
                            break;
                        }
                        /*else if ((i *j ) ==(14 * j) && j !=0 ){
                            break;
                        }*/
                        Log.e("out2  ", list.get(x));
                        canvas.drawText(items.get(i).getADDRESS_DETAILS().substring(0, items.get(i).getADDRESS_DETAILS().length() / 3), 890.0f, 360 + pos, paint);
                        canvas.drawText(items.get(i).getADDRESS_DETAILS().substring(items.get(i).getADDRESS_DETAILS().length() / 3, (items.get(i).getADDRESS_DETAILS().length() / 3) * 2), 890.0f, 395 + pos, paint);
                        canvas.drawText(items.get(i).getADDRESS_DETAILS().substring((items.get(i).getADDRESS_DETAILS().length() / 3) * 2, items.get(i).getADDRESS_DETAILS().length()), 890.0f, 420 + pos, paint);
                        canvas.drawText(items.get(i).getCUSTOMER_NAME(), 1570.0f, 390 + pos, paint);
                        canvas.drawText(items.get(i).getCUSTOMER_PHONE(), 1200.0f, 390 + pos, paint);
                        paint.setTextSize(22.0f);
                        canvas.drawText(items.get(i).getDelivery_Method(), 1730.0f, 390 + pos, paint);
                        canvas.drawText(items.get(i).getPayment_Method(), 1910.0f, 390 + pos, paint);
                        paint.setTextSize(30.0f);
                        canvas.drawText(items.get(i).getOUTBOUND_DELIVERY(), 2850.0f, 390 + pos, paint);
                        canvas.drawText(String.valueOf(i + 1), 2910.0f, 390 + pos, paint);
                        canvas.drawText(String.valueOf(new DecimalFormat("##.00").format(Float.valueOf(items.get(i).getITEM_PRICE()))), 2070.0f, 390 + pos, paint);
                        total += Float.valueOf(items.get(i).getITEM_PRICE());
                        try {
                            testCODE93(canvas, 2120.0f, 340 + pos, items.get(i).getTRACKING_NO());
                            canvas.drawLine(30.0f, 430.0f + pos, 2940.0f, 430.0f + pos, paint2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        pos += 100;

                    }
              // }

                canvas.drawText("اجمالي الطلب:", 2500.0f, 390 + pos, paint);
                canvas.drawText(String.valueOf(new DecimalFormat("##.00").format(Float.valueOf(Response_RecordsHeader_list_for_runtimesheet_Orders.get(x).getGRAND_TOTAL()) + Float.valueOf(Response_RecordsHeader_list_for_runtimesheet_Orders.get(x).getReedemed_Points_Amount()))), 2070.0f, 390 + pos, paint);
                canvas.drawText("قيمه النقاط المستبدلة:", 1550.0f, 390 + pos, paint);
                canvas.drawText(String.valueOf(new DecimalFormat("##0.00").format(Float.valueOf(Response_RecordsHeader_list_for_runtimesheet_Orders.get(x).getReedemed_Points_Amount()))), 1200.0f, 390 + pos, paint);
                canvas.drawText("المطلوب تحصيله:", 890.0f, 390 + pos, paint);
                canvas.drawText(String.valueOf(new DecimalFormat("##.00").format(Float.valueOf(Response_RecordsHeader_list_for_runtimesheet_Orders.get(x).getGRAND_TOTAL()))), 290.0f, 390 + pos, paint);
                canvas.drawLine(30.0f, 400.0f + pos, 2940.0f, 400.0f + pos, paint2);
                pos += 100;
            }


            pdfDocument.finishPage(page);
            try {
                pdfDocument.writeTo(new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "/HyperOne.pdf")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pdfDocument.close();

    }
    public String checkPaymentMethod(String name)
    {
        if (name.equals("0") || name.equals("0.000")) {
            return "أون لاين";
        }
        else
        {return "كاش";}
    }

    private static void testCODE93(Canvas canvas , float left, float top,String trackingnumber) throws Exception
    {
        Code93 barcode = new Code93();

        /*
           Code 93 Valid data char set:
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9 (Digits)
                A - Z (Uppercase letters)
                - (Dash), $ (Dollar), % (Percentage), (Space), . (Point), / (Slash), + (Plus)
        */
        barcode.setData(trackingnumber);

        // Unit of Measure, pixel, cm, or inch
        barcode.setUom(IBarcode.UOM_PIXEL);
        // barcode bar module width (X) in pixel
        barcode.setX(3f);
        // barcode bar module height (Y) in pixel
        barcode.setY(60f);

        // barcode image margins
//        barcode.setLeftMargin(1f);
//        barcode.setRightMargin(1f);
//        barcode.setTopMargin(1f);
//        barcode.setBottomMargin(1f);

        // barcode image resolution in dpi
        barcode.setResolution(72);

        // disply barcode encoding data below the barcode
        barcode.setShowText(true);
        // barcode encoding data font style
        barcode.setTextFont(new AndroidFont("Arial", Typeface.NORMAL, 26));
        // space between barcode and barcode encoding data
        barcode.setTextMargin(10);
        barcode.setTextColor(AndroidColor.black);

        // barcode bar color and background color in Android device
        barcode.setForeColor(AndroidColor.black);
        barcode.setBackColor(AndroidColor.white);

        /*
        specify your barcode drawing area
	    */
        RectF bounds = new RectF(left, top, 0, 0);
        barcode.drawBarcode(canvas, bounds);
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
}