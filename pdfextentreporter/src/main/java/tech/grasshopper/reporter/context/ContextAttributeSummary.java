package tech.grasshopper.reporter.context;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.Status;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.font.ReportFont;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ContextAttributeSummary extends AttributeSummaryDisplay {

	private static final float HEADER_NAME_WIDTH = 215f;
	private static final float HEADER_PASS_WIDTH = 45f;
	private static final float HEADER_FAIL_WIDTH = 45f;
	private static final float HEADER_SKIP_WIDTH = 45f;
	private static final float HEADER_WARN_WIDTH = 45f;
	private static final float HEADER_INFO_WIDTH = 45f;
	private static final float HEADER_PASS_PERCENT_WIDTH = 60f;

	@Default
	private Map<String, Map<Status, Integer>> data = new LinkedHashMap<>();

	@Override
	public void display() {

		data = contextAttributeData();

		createTableBuilder();
		createTitleRow();
		createHeaderRow();
		createDataRows();
		drawTable();
	}

	private void createTableBuilder() {
		tableBuilder = Table.builder()
				.addColumnsOfWidth(HEADER_NAME_WIDTH, HEADER_PASS_WIDTH, HEADER_FAIL_WIDTH, HEADER_SKIP_WIDTH,
						HEADER_WARN_WIDTH, HEADER_INFO_WIDTH, HEADER_PASS_PERCENT_WIDTH)
				.padding(HEADER_PADDING).borderColor(Color.LIGHT_GRAY).borderWidth(BORDER_WIDTH)
				.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.TOP);
	}

	private void createTitleRow() {
		tableBuilder.addRow(Row.builder().height(NAME_HEIGHT).font(ReportFont.BOLD_ITALIC_FONT).fontSize(NAME_FONT_SIZE)
				.borderWidth(0)
				.add(TextCell.builder().text(type.toString()).textColor(config.attributeHeaderColor(type))
						.horizontalAlignment(HorizontalAlignment.LEFT).colSpan(7).build())
				.build());
	}

	private void createHeaderRow() {
		tableBuilder.addRow(Row.builder().height(HEADER_HEIGHT).font(ReportFont.ITALIC_FONT).fontSize(HEADER_FONT_SIZE)
				.add(TextCell.builder().text("Name").textColor(config.attributeNameColor(type))
						.font(ReportFont.BOLD_ITALIC_FONT).lineSpacing(MULTILINE_SPACING)
						.horizontalAlignment(HorizontalAlignment.LEFT).build())
				.add(TextCell.builder().text("Pass").textColor(config.getPassColor()).build())
				.add(TextCell.builder().text("Fail").textColor(config.getFailColor()).build())
				.add(TextCell.builder().text("Skip").textColor(config.getSkipColor()).build())
				.add(TextCell.builder().text("Warn").textColor(config.getWarnColor()).build())
				.add(TextCell.builder().text("Info").textColor(config.getInfoColor()).build())
				.add(TextCell.builder().text("Pass %").textColor(config.getPassColor()).build()).build());
	}

	private void createDataRows() {
		data.forEach((k, v) -> {
			int passpercent = (v.getOrDefault(Status.PASS, 0) * 100)
					/ (v.values().stream().mapToInt(Integer::intValue).sum());
			Row row = Row.builder().font(TABLE_CONTENT_FONT).fontSize(TABLE_CONTENT_FONT_SIZE).wordBreak(true)
					.padding(TABLE_PADDING)
					.add(TextCell.builder().text(textSanitizer.sanitizeText(k)).lineSpacing(MULTILINE_SPACING)
							.textColor(config.attributeNameColor(type)).horizontalAlignment(HorizontalAlignment.LEFT)
							.build())
					.add(TextCell.builder().text(String.valueOf(v.getOrDefault(Status.PASS, 0)))
							.textColor(config.getPassColor()).build())
					.add(TextCell.builder().text(String.valueOf(v.getOrDefault(Status.FAIL, 0)))
							.textColor(config.getFailColor()).build())
					.add(TextCell.builder().text(String.valueOf(v.getOrDefault(Status.SKIP, 0)))
							.textColor(config.getSkipColor()).build())
					.add(TextCell.builder().text(String.valueOf(v.getOrDefault(Status.WARNING, 0)))
							.textColor(config.getWarnColor()).build())
					.add(TextCell.builder().text(String.valueOf(v.getOrDefault(Status.INFO, 0)))
							.textColor(config.getInfoColor()).build())
					.add(TextCell.builder().text(String.valueOf(passpercent)).textColor(config.getPassColor()).build())
					.build();

			tableBuilder.addRow(row);
		});
	}
}
