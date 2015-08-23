package com.kileyowen.neuralcoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
	private File file;

	public CodeWriter(String folder, String fileName) {
		file = new File(folder);
		file.mkdirs();
		file = new File(folder + fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeCode(String code) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(code);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int compilePython() {
		try {
			Process pro = Runtime.getRuntime().exec("python -m py_compile " + file.getAbsolutePath());
			pro.waitFor();
			return pro.exitValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public int compileJava() {
		try {
			Process pro = Runtime.getRuntime().exec("javac " + file.getAbsolutePath());
			pro.waitFor();
			return pro.exitValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
}
