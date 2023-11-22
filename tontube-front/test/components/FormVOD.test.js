import { shallowMount } from "@vue/test-utils";
import FormVOD from "@/components/FormVOD.vue";

let wrapper;

beforeEach(() => {
  wrapper = shallowMount(FormVOD, {
    propsData: {
      isUpdate: false,
    },
    mocks: {},
    stubs: {},
    methods: {},
  });
});

afterEach(() => {
  wrapper.destroy();
});

describe("FormVOD", () => {
  test("is a vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  test("props", () => {
    expect(wrapper.props()).toStrictEqual({
      vodToModify: null,
      isUpdate: false,
    });
  });

  test("data", () => {
    expect(wrapper.vm.vodData).toStrictEqual({
      title: "", // title of the video
      description: "", // description of the video
      tags: [], // array
      privacy: "public", // public, private, unlisted
      file: "", // video file
    });
    expect(wrapper.vm.isFile).toBe(false);
    expect(wrapper.vm.newTag).toBe("");
  });
});
