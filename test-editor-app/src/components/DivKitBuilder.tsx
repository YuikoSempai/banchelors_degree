import { Div, divCard, DivContainer, DivText, reference, template, templateHelper, rewriteRefs, DivFixedSize, DivSolidBackground, IDivData, ITemplates } from '@divkitframework/jsonbuilder';
import { useEffect } from 'react';
import * as fabric from 'fabric';

class DivKitBuilder {

    private static buttonTemplate = {
        _button_template: new DivText({
            text: reference('text'),
            background: [
                {
                    type: "solid",
                    color: "#000"
                }
            ],
            text_color: "#fff",
            border: {
                corner_radius: 4
            },
            width: {
                type: "wrap_content",
                constrained: true
            },
            alignment_horizontal: "center",
            alignment_vertical: "center"
        })
    }

    private static mapRectangleToButton(obj: fabric.Object): DivText | null {
        console.log(obj)
        if (obj.type != "group") {
            return null
        }
        return new DivText({
            text: "example text",
            background: [
                {
                    type: "solid",
                    color: "#000"
                }
            ],
            text_color: "#fff",
            border: {
                corner_radius: 4
            },
            width: {
                type: "wrap_content",
                constrained: true
            },
            margins: {
                start: Math.floor(obj.left),
                top: Math.floor(obj.top)
            },
            actions: [
                {
                  log_id: "action_id",
                  typed: {
                    type: "set_variable",
                    value: {
                      type: "boolean",
                      value: ((obj as fabric.Group).item(0)).isCorrect
                    },
                    variable_name: "is_correct"
                  }
                }
              ],
        })
    }

    private static buttonTemplateHelper = templateHelper(this.buttonTemplate)


    private static textFieldTemplate = {
        _text_field_template: new DivText({
            text: "text",
            font_size: 20,
            width: {
                type: "wrap_content",
                constrained: true
            },
            alignment_horizontal: "left",
            alignment_vertical: "top",
            margins: {
                left: 242,
                top: 226
            }
        })
    }

    private static templates = {
        _template_container: new DivContainer({
            items: [
                template('header', {
                    text: reference('title')
                }),
                template('header', {
                    text: reference('subtitle')
                }),
                template('_button_template', {
                    text: reference("rectTextField1")
                }),
                template('_button_template', {
                    text: reference("rectTextField2")
                }),
                template('_button_template', {
                    text: reference("rectTextField3")
                }),
                template('_button_template', {
                    text: reference("rectTextField4")
                })
            ],
        }),
        header: new DivText({
            font_size: 24,
            text: "example text"
        }),
        _button_template: this.buttonTemplate._button_template
    };

    private static tHelper = templateHelper(DivKitBuilder.templates);

    static createCanvasObjects(objects: fabric.Object[]): ({ templates: ITemplates; card: IDivData; }) {
        // return divCard(rewriteRefs(this.templates), {
        return {
            card: {
                log_id: 'sample_card',
                states: [
                    {
                        state_id: 0,
                        div: new DivContainer({
                            items: objects.map(it => {
                                // if (it.type == 'rect') {
                                //     console.log("is rect")
                                //     let button = this.mapRectangleToButton(it)
                                //     console.log(JSON.stringify(button))
                                //     return button
                                // } else if (it.type == 'i-text') {
                                //     console.log("is i-text")
                                //     it
                                // }
                                return this.mapRectangleToButton(it)
                            }).filter(value => !!value),
                            background: [
                                {
                                    color: "#FFFFFF",
                                    type: "solid"
                                }
                            ],
                            orientation: "overlap",
                            width: {
                                type: "fixed",
                                value: 350
                            },
                            height: {
                                type: "fixed",
                                value: 650
                            }
                        }),
                    }
                ],
            },
            templates: {}
        }
    }
}

export default DivKitBuilder