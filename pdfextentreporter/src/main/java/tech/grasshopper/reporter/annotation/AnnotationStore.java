package tech.grasshopper.reporter.annotation;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import tech.grasshopper.pdf.annotation.Annotation;

@Data
public class AnnotationStore {

	private List<Annotation> testNameAnnotation = new ArrayList<>();

	private List<Annotation> attributeNameAnnotation = new ArrayList<>();

	public void addTestNameAnnotation(Annotation annotation) {
		testNameAnnotation.add(annotation);
	}

	public void addAttributeNameAnnotation(Annotation annotation) {
		attributeNameAnnotation.add(annotation);
	}
}