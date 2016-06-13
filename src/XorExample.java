import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.joone.engine.*;
import org.joone.engine.learning.*;
import org.joone.io.*;
import org.joone.net.NeuralNet;
import org.joone.net.NeuralNetLoader;

/**
 * Example: The XOR Problem with JOONE
 *
 * @author Ming Sun
 * @version 1.0
 */
public class XorExample implements
ActionListener,NeuralNetListener {

FullSynapse t1,t2;

  /**
   * The train button.
   */


  /**
   * Constructor. Set up the components.
   */
  public XorExample()
  {

  }
  /**
   * Called when the user clicks one of the three
   * buttons.
   * 
   * @param e The event.
   */
  public void actionPerformed(ActionEvent e)
  {
     // run();
  }
  
  //���к����ڼ�������
  private static final long serialVersionUID = -3472219226214066504L;
  //�������Ա
  private NeuralNet nnet = null;
  //�����籣��·��
  private String NNet_Path = null;

  /**��ʼ�������� 
   * String NNet_Path ��������·��
   * int InputNum     �������Ԫ����
   * int HiddenNum    ���ز���Ԫ����
   * int OutputNum    �������Ԫ����
   * */
  public void Init_BPNN(String NNet_Path,int InputNum,int HiddenNum,int OutputNum) {
      //����������ı���·��
      this.NNet_Path = NNet_Path;
      //�½����Layer���ֱ���Ϊ����㣬���ز㣬�����
      LinearLayer input = new LinearLayer();
      SigmoidLayer hidden = new SigmoidLayer();
      SigmoidLayer output = new SigmoidLayer();
      //����ÿ��Layer�����Ԫ����
      input.setRows(InputNum);
      hidden.setRows(HiddenNum);
      output.setRows(OutputNum);
      //�½�����ͻ�����������Ӹ���
      FullSynapse synapse_IH = new FullSynapse();
      FullSynapse synapse_HO = new FullSynapse();
      //��������-���أ�����-�������
      input.addOutputSynapse(synapse_IH);
      hidden.addInputSynapse(synapse_IH);
      hidden.addOutputSynapse(synapse_HO);
      output.addInputSynapse(synapse_HO);
      //�½�һ�������磬���������㣬���ز㣬�����
      nnet = new NeuralNet();
      nnet.addLayer(input, NeuralNet.INPUT_LAYER);
      nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
      nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
  }

  /**ʹ�ô����ļ�ѵ���½��������磬����Init_BPNN����ʹ��
   * String TrainFile ѵ���ļ����·��
   * int TrainLength  ѵ���ļ�������
   * double Rate      ������ѵ���ٶ�
   * double Momentum  ������ѵ������
   * int TrainCicles  ������ѵ������
   * */
  public void Train_BPNN(String TrainFile,int TrainLength,double Rate,double Momentum,int TrainCicles){   
      //��ȡ�����
      Layer input = nnet.getInputLayer();     
      //�½�����ͻ��
      FileInputSynapse trains = new FileInputSynapse();
      //���������ļ�
      trains.setInputFile(new File(TrainFile));
      //����ʹ�õ������ļ���1��2��3��4����Ϊѵ��������
      //trains.setAdvancedColumnSelector("1,2,3,4,5,6,7,8,9,10,11,12,13");
      trains.setAdvancedColumnSelector("1,2,3,4");
      //��ȡ�����
      Layer output = nnet.getOutputLayer();
      //�½�����ͻ��
      FileInputSynapse target = new FileInputSynapse();
      //���������ļ�
      target.setInputFile(new File(TrainFile));
      //����ʹ�õ������ļ���5��6����Ϊѵ����Ŀ��
      target.setAdvancedColumnSelector("5");

      //�½�ѵ��ͻ��
      TeachingSynapse trainer = new TeachingSynapse();
      //����ѵ��Ŀ��
      trainer.setDesired(target);

      //�������������ͻ��
      input.addInputSynapse(trains);
      //������������ͻ��
      output.addOutputSynapse(trainer);       
      //�����������ѵ��ͻ��
      nnet.setTeacher(trainer);

      //��ȡ������ļ�����
      Monitor monitor = nnet.getMonitor();
      //����ѵ������
      monitor.setLearningRate(Rate);
      //����ѵ������
      monitor.setMomentum(Momentum);
      //����������
      monitor.addNeuralNetListener(this);// ������
      //����ѵ����ݸ�������
      monitor.setTrainingPatterns(TrainLength);
      //����ѵ������
      monitor.setTotCicles(TrainCicles); 
      //��ѵ��ģʽ
      monitor.setLearning(true);
      //��ʼѵ��
      nnet.go();
  }
  
  /**ʹ�ô����ļ�����ѵ�����������
   * String NNet_Path ��������·��
   * String OutFile   ���Խ����·��    
   * String TestFile  ѵ���ļ����·��
   * int TestLength   ѵ���ļ�������
   * */
  public double Test_BPNN(String NNet_Path,double[] inputs, final HttpServletResponse response,int TestLength){
      NeuralNet testBPNN = this.restoreNeuralNet(NNet_Path);
      if (testBPNN != null) { 

         Layer input = testBPNN.getInputLayer();
          Pattern pattern=new Pattern();
          /*
          Vector vector=new Vector();
          Pattern pattern=new Pattern();
          vector.add(0.62);
          vector.add(0.0);
          vector.add(0.4);
          vector.add(0.14);
          vector.add(0.268);
          vector.add(0.0);
          vector.add(0.2);
          vector.add(0.16);
          vector.add(0.0);
          vector.add(0.36);
          vector.add(0.3);
          vector.add(0.2);
          vector.add(0.3);
          */
       //   0.62;0.0;0.4;0.14;0.268;0.0;0.2;0.16;0.0;0.36;0.3;0.2;0.3;1.0
          Double[] doubles=new Double[13];
        //  vector.toArray(doubles);
        //  0.57;1.0;0.3;0.15;0.168;0.0;0.0;0.174;0.0;0.16;0.1;0.0;0.3;0.0

//          aa[13]=1.0;
          pattern.setValues(inputs);
         // pattern.setValue(1,0.6);
          Vector vector1=new Vector();
          vector1.add(pattern);
          FileInputSynapse inputStream = new FileInputSynapse();
         inputStream.setInputPatterns(vector1);
        //  input.fwdRun(pattern);
        //  testBPNN.setInputLayer();
         // inputStream.setInputFile();
        //  inputStream.setInputFile(new File(TestFile));
         // inputStream.setAdvancedColumnSelector("1,2,3,4,5,6,7,8,9,10,11,12,13");
          input.removeAllInputs();
        //  input.setAllInputs();
          input.addInputSynapse(inputStream);
        //  input.setAllInputs();
       //   0.62;0.0;0.4;0.14;0.268;0.0;0.2;0.16;0.0;0.36;0.3;0.2;0.3

        //  input.setAllInputs(vector);

         // testBPNN.setAllInputs(vector);
          Layer output = testBPNN.getOutputLayer();
          //�������ͻ��
          final resultOut fileOutput = new resultOut();
          //��������ļ�����·��
        //  fileOutput.fwdGet();
        //  fileOutput.setFileName(OutFile);
          output.addOutputSynapse(fileOutput);
          double[] dou=new double[1];
        //  Pattern pattern1=new Pattern(dou);
        //  fileOutput.write(pattern1);
          Monitor monitor = testBPNN.getMonitor();
          monitor.setTrainingPatterns(TestLength);
          monitor.setTotCicles(1);
          //�ر�ѵ��ģʽ
          monitor.setLearning(false);
       //   monitor.
          //��ʼ����
         // testBPNN.go();
          testBPNN.go(false,true);
          /*
          monitor.addNeuralNetListener(new NeuralNetListener() {
              @Override
              public void netStarted(NeuralNetEvent neuralNetEvent) {

              }

              @Override
              public void cicleTerminated(NeuralNetEvent neuralNetEvent) {

              }

              @Override
              public void netStopped(NeuralNetEvent neuralNetEvent) {
//System.out.println("结果");
                  String s=fileOutput.getResult()+"";
                  System.out.println("s="+s);
                  s="\r\n"+s+"\r\n";
                  try
                  {
                      OutputStream os=null;
                      os=response.getOutputStream();
                      response.setHeader("Content-Length", s.getBytes("gbk").length + "");
                      byte[] bytes=s.getBytes("gbk");
                      os.write(bytes);
                      os.flush();
                      os.close();
                  }
                  catch (Exception e)
                  {
                      e.printStackTrace();
                  }
                  predictSevelt.exit=1;
                  // out.println(s);
                  //  out.println("lt"+req.getHeaderNames().toString());
                 // return fileOutput.getResult();
              }

              @Override
              public void errorChanged(NeuralNetEvent neuralNetEvent) {

              }

              @Override
              public void netStoppedError(NeuralNetEvent neuralNetEvent, String s) {

              }
          });
          */
       //   while (Thread.activeCount()>=2)
       //   {
//
  //            System.out.println(monitor.);
   //       }
        System.out.println("size=" + output.getAllOutputs().size());
     //     Object vector2=output.getAllOutputs().get(1);
     //     System.out.println(vector2);
//          FileOutputSynapse f=(FileOutputSynapse)vector2;
        //  f.write(pattern);
      //    f.fwdPut(pattern);
        //  System.out.println(pattern1.getArray()[0]);
          System.out.println("test"+fileOutput.getResult());
          return fileOutput.getResult();
      }
      return -1;
  }
    //开始预测
    public double startPredict(String NNet_Path,double[] inputs,int TestLength){
        NeuralNet testBPNN = this.restoreNeuralNet(NNet_Path);
        if (testBPNN != null) {

            Layer input = testBPNN.getInputLayer();
            Pattern pattern=new Pattern();
            pattern.setValues(inputs);
            Vector vector1=new Vector();
            vector1.add(pattern);
            FileInputSynapse inputStream = new FileInputSynapse();
            inputStream.setInputPatterns(vector1);
            input.removeAllInputs();
            input.addInputSynapse(inputStream);
            Layer output = testBPNN.getOutputLayer();
            final resultOut fileOutput = new resultOut();
            output.addOutputSynapse(fileOutput);
            double[] dou=new double[1];
            Monitor monitor = testBPNN.getMonitor();
            monitor.setTrainingPatterns(TestLength);
            monitor.setTotCicles(1);
            monitor.setLearning(false);
            testBPNN.go(false,true);
            System.out.println("size=" + output.getAllOutputs().size());
            System.out.println("test"+fileOutput.getResult());
            return fileOutput.getResult();
        }
        return -1;
    }
  /**�������е�������
   * String NNet_Path ��������·��
   * */
  public double Test(String NNet_Path,String OutFile,String TestFile,int TestLength){
      NeuralNet testBPNN = this.restoreNeuralNet(NNet_Path);
      if (testBPNN != null) {

          Layer input = testBPNN.getInputLayer();
          /**�����ļ�������� */
          Vector vector=new Vector();
          Pattern pattern=new Pattern();
          vector.add(0.62);
          vector.add(0.0);
          vector.add(0.4);
          vector.add(0.14);
          vector.add(0.268);
          vector.add(0.0);
          vector.add(0.2);
          vector.add(0.16);
          vector.add(0.0);
          vector.add(0.36);
          vector.add(0.3);
          vector.add(0.2);
          vector.add(0.3);
          //   0.62;0.0;0.4;0.14;0.268;0.0;0.2;0.16;0.0;0.36;0.3;0.2;0.3;1.0
          Double[] doubles=new Double[13];
          vector.toArray(doubles);
          //  0.57;1.0;0.3;0.15;0.168;0.0;0.0;0.174;0.0;0.16;0.1;0.0;0.3;0.0
          double[] aa=new double[13];
          /*
          aa[0]=0.57;
          aa[1]=1.0;
          aa[2]=0.3;
          aa[3]=0.15;
          aa[4]=0.168;
          aa[5]=0.0;
          aa[6]=0.0;
          aa[7]=0.174;
          aa[8]=0.0;
          aa[9]=0.16;
          aa[10]=0.1;
          aa[11]=0.0;
          aa[12]=0.3;
          */
          //  0.38;1.0;0.1;0.12;0.231;0.0;0.0;0.182;1.0;0.38;0.2;0.0;0.7;1.0
          aa[0]=0.38;
          aa[1]=0.0;
          aa[2]=0.1;
          aa[3]=0.12;
          aa[4]=0.231;
          aa[5]=0.0;
          aa[6]=0.0;
          aa[7]=0.182;
          aa[8]=1.0;
          aa[9]=0.38;
          aa[10]=0.2;
          aa[11]=0.0;
          aa[12]=0.7;
//          aa[13]=1.0;
          pattern.setValues(aa);
          // pattern.setValue(1,0.6);
          Vector vector1=new Vector();
          vector1.add(pattern);
          FileInputSynapse inputStream = new FileInputSynapse();
          inputStream.setInputPatterns(vector1);
          //  input.fwdRun(pattern);
          //  testBPNN.setInputLayer();
          // inputStream.setInputFile();
          //  inputStream.setInputFile(new File(TestFile));
          // inputStream.setAdvancedColumnSelector("1,2,3,4,5,6,7,8,9,10,11,12,13");
          input.removeAllInputs();
          //  input.setAllInputs();
          input.addInputSynapse(inputStream);
          //  input.setAllInputs();
          //   0.62;0.0;0.4;0.14;0.268;0.0;0.2;0.16;0.0;0.36;0.3;0.2;0.3

          //  input.setAllInputs(vector);

          // testBPNN.setAllInputs(vector);
          Layer output = testBPNN.getOutputLayer();
          //�������ͻ��
          final resultOut fileOutput = new resultOut();
          //��������ļ�����·��
          //  fileOutput.fwdGet();
          //  fileOutput.setFileName(OutFile);
          output.addOutputSynapse(fileOutput);
          double[] dou=new double[1];
          //  Pattern pattern1=new Pattern(dou);
          //  fileOutput.write(pattern1);
          Monitor monitor = testBPNN.getMonitor();
          monitor.setTrainingPatterns(TestLength);
          monitor.setTotCicles(1);
          //�ر�ѵ��ģʽ
          monitor.setLearning(false);
          //   monitor.
          //��ʼ����
          testBPNN.go();
          monitor.addNeuralNetListener(new NeuralNetListener() {
              @Override
              public void netStarted(NeuralNetEvent neuralNetEvent) {

              }

              @Override
              public void cicleTerminated(NeuralNetEvent neuralNetEvent) {

              }

              @Override
              public void netStopped(NeuralNetEvent neuralNetEvent) {
                  System.out.println(fileOutput.getResult());
                  // return fileOutput.getResult();
              }

              @Override
              public void errorChanged(NeuralNetEvent neuralNetEvent) {

              }

              @Override
              public void netStoppedError(NeuralNetEvent neuralNetEvent, String s) {

              }
          });
          //   while (Thread.activeCount()>=2)
          //   {
//
          //            System.out.println(monitor.);
          //       }
          System.out.println("size=" + output.getAllOutputs().size());
          //     Object vector2=output.getAllOutputs().get(1);
          //     System.out.println(vector2);
//          FileOutputSynapse f=(FileOutputSynapse)vector2;
          //  f.write(pattern);
          //    f.fwdPut(pattern);
          //  System.out.println(pattern1.getArray()[0]);
          System.out.println("test"+fileOutput.getResult());
      }
      return -1;
  }
  NeuralNet Get_BPNN(String NNet_Path) {
      NeuralNetLoader loader = new NeuralNetLoader(NNet_Path);
      NeuralNet nnet = loader.getNeuralNet();
      return nnet;
  }
  /**
   * Called when the user clicks the run button.
   */
  protected double run(double[] inputs,HttpServletResponse response)
  {
	  return  Test_BPNN("/Users/novas/Desktop/xor.snet",inputs,response,297);
	 // status.setText("Results written to " + resultFile.getText());
  }

  /**
   * Called when the user clicks the train button.
   */
  protected void train()
  {
	Init_BPNN("fff_xor.snet",4,6,1);
	Train_BPNN("train_B0005.txt",164,0.8 ,0.3 ,20000);
  }

  /**
   * JOONE Callback: called when the neural network
   * stops. Not used.
   * 
   * @param e The JOONE event
   */
  public void netStopped(NeuralNetEvent e) {
	  saveNeuralNet("xor.snet"); // ������ɵ�ǰʱ���myxor.snet������
  }

  /**
   * JOONE Callback: called to update the progress
   * of the neural network. Used to update the
   * status line.
   * 
   * @param e The JOONE event
   */
  public void cicleTerminated(NeuralNetEvent e) {
    Monitor mon = (Monitor)e.getSource();
    long c = mon.getCurrentCicle();
    long cl = c / 1000;
    // print the results every 1000 cycles

  }
  
  public void saveNeuralNet(String fileName) {  
      try {  
          FileOutputStream stream = new FileOutputStream(fileName);  
          ObjectOutputStream out = new ObjectOutputStream(stream);  
          out.writeObject(nnet);// д��nnet����  
          out.close();  
      } catch (Exception excp) {  
          excp.printStackTrace();  
      }  
  }  
    
  NeuralNet restoreNeuralNet(String fileName) {  
      NeuralNetLoader loader = new NeuralNetLoader(fileName);  
      NeuralNet nnet = loader.getNeuralNet();  
      return nnet;  
      } 

  /**
   * JOONE Callback: Called when the network
   * is starting up. Not used.
   * 
   * @param e The JOONE event.
   */
  public void netStarted(NeuralNetEvent e) {
  }

@Override
public void errorChanged(NeuralNetEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void netStoppedError(NeuralNetEvent arg0, String arg1) {
	// TODO Auto-generated method stub
	
}

}
