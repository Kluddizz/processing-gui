package com.hansen.processing.ui.xml;

import com.hansen.processing.ui.events.NotifyPropertyChanged;
import com.hansen.processing.ui.controls.*;

import javax.xml.bind.*;

import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Class to load control elements out of XML files
 * 
 * @author Florian Hansen
 *
 */
public class XMLLoader {
	
	private static JAXBContext createProcessingUiContext() throws JAXBException {
		return JAXBContext.newInstance(CheckBox.class, MenuItem.class, TextBlock.class, Grid.class,
				Button.class, Panel.class, RowDefinition.class, ColumnDefinition.class, StackPanel.class,
				ViewportContainer.class, TextBox.class, Label.class, ScrollViewer.class);
	}

	/**
	 * Loads a control based on a URL
	 * 
	 * @param <T>
	 * @param file
	 * @return
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Control> T load(URL file) throws JAXBException, FileNotFoundException {
		if (file == null) {
			throw new FileNotFoundException();
		}

		JAXBContext context = createProcessingUiContext();
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new ValidationEventHandler() {

			@Override
			public boolean handleEvent(ValidationEvent validationEvent) {
				return false;
			}

		});

		// unmarshalling and initialization of the loaded component
		T component = (T) unmarshaller.unmarshal(file);
		
		if (component.getController() != null) {
			component.getController().target = component;
			component.getController().initController();			
		}

		return component;
	}

	/**
	 * Loads a control based on a URL
	 * 
	 * @param <T>
	 * @param file
	 * @return
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Control> T load(URL file, Object controllerTarget)
			throws JAXBException, FileNotFoundException {
		if (file == null) {
			throw new FileNotFoundException();
		}

		JAXBContext context = createProcessingUiContext();
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new ValidationEventHandler() {

			@Override
			public boolean handleEvent(ValidationEvent validationEvent) {
				return false;
			}

		});

		// unmarshalling and initialization of the loaded component
		T component = (T) unmarshaller.unmarshal(file);

		if (component.getController() != null) {
			component.getController().target = controllerTarget;

			component.getController().initController();
			component.initControl(component.getController());
		}

		return component;
	}

	/**
	 * Loads a control based on a URL and assign a controller to it.
	 * 
	 * @param <T>
	 * @param file
	 * @param context
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Control> T load(URL file, NotifyPropertyChanged context) throws JAXBException {
		JAXBContext jaxbContext = createProcessingUiContext();
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setEventHandler(new ValidationEventHandler() {

			@Override
			public boolean handleEvent(ValidationEvent validationEvent) {
				return false;
			}

		});

		T component = (T) unmarshaller.unmarshal(file);
		component.getController().target = component;

		component.getController().initController();
		component.initControl(context);

		return component;
	}

}
