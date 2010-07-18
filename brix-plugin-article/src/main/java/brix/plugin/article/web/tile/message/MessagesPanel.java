/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package brix.plugin.article.web.tile.message;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import brix.jcr.wrapper.BrixNode;

/**
 * @author wickeria at gmail.com
 */
public class MessagesPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private boolean addMessageVisible = false;
	private boolean messageListVisible = false;

	public MessagesPanel(String id, final IModel<BrixNode> model, boolean isGuestbook) {
		super(id, model);
		setOutputMarkupId(true);
		messageListVisible = isGuestbook;
		add(new AjaxLink<Void>("prev") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				messageListVisible = !messageListVisible;
				target.addComponent(MessagesPanel.this);
			}

			@Override
			public boolean isVisible() {
				return !messageListVisible;
			}
		});
		add(new Label("label", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return "- " + model.getObject().getNodes("entry").getSize() + " messages";
			}
		}));
		add(new AddMessagePanel("addMessagePanel", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return addMessageVisible;
			}

			@Override
			protected void onOk(AjaxRequestTarget target) {
				addMessageVisible = false;
				target.addComponent(MessagesPanel.this);
			}

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				addMessageVisible = false;
				target.addComponent(MessagesPanel.this);
			}
		});

		add(new MessageListPanel(!isGuestbook ? "messageListTop" : "messageListBottom", model, !isGuestbook) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return messageListVisible;
			}
		});
		add(new EmptyPanel(!isGuestbook ? "messageListBottom" : "messageListTop"));
		add(new AjaxLink<Void>("addMessage") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				addMessageVisible = !addMessageVisible;
				target.addComponent(MessagesPanel.this);
			}

			@Override
			public boolean isVisible() {
				return messageListVisible && !addMessageVisible;
			}
		});

	}
}