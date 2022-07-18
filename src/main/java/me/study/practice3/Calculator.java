package me.study.practice3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator<T> {

    /*
     * 남아있던 반복되는 로직을 템플릿에 추가하는 리팩토링 작업
     * */
    public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            // 버퍼의 각 라인을 읽는 동작을 템플릿에 추가
            String line = null;
            T res = initVal;
            while ((line = br.readLine()) != null) {
                // 콜백 객체에서는 순수한 계산 로직만 담당하도록 수정, 보완
                // 코드의 관심사를 명확히 분리
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /*
     * 변하지 않는 컨텍스트를 갖는 템플릿 메소드 분리
     * */
    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            int ret = callback.doSomethingWithReader(br); // 변경이 발생하는 부분은 콜백 객체에서 처리
            return ret;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /*
    * 보완된 템플릿과 콜백을 활용하는 클라이언트 메소드
    * */
    public Integer clacSumWithLine(String filepath) throws IOException {
        return this.lineReadTemplate(filepath, new LineCallback<Integer>() { // 순수한 계산 로직만 갖는 구현체
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        }, 0);
    }
    public Integer clacMultiplyWithLine(String filepath) throws IOException  {
        return this.lineReadTemplate(filepath, new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        }, 1);
    }
    public String concatenate(String filepath) throws IOException  {
        return this.lineReadTemplate(filepath, new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        }, "");
    }

    /*
    * 템플릿/콜백을 활용하늩 클라이언트 메소드
    * */
    public Integer clacSum(String filepath) throws IOException {
        return this.fileReadTemplate(filepath, new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer sum = 0;
                String line = null;

                while ((line = br.readLine()) != null) {
                    sum += Integer.valueOf(line);
                }
                return sum;
            }
        });
    }

    public Integer clacMultiply(String filepath) throws IOException  {
        return this.fileReadTemplate(filepath, new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer multiply = 1;
                String line = null;

                while ((line = br.readLine()) != null) {
                    multiply *= Integer.valueOf(line);
                }
                return multiply;
            }
        });
    }

    // try.catch.finally 블록을 사용하며 여러군데에서 반복적으로 활용되는 코드는
    // template-callback 패턴을 적용할 수 있다.
    public Integer _clacSum(String filepath) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            Integer sum = 0;
            String line = null;

            while ((line = br.readLine()) != null) {
                sum += Integer.valueOf(line);
            }
            return sum;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                    System.out.println(e.getMessage());
                }
            }
        }
    }

}