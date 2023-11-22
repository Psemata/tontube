import { shallowMount } from "@vue/test-utils";
import VideoPanel from "@/components/VideoPanel.vue";

let wrapper;

beforeEach(() => {
  wrapper = shallowMount(VideoPanel, {
    propsData: {
      vods: [],
    },
    mocks: {},
    stubs: {},
    methods: {},
  });
});

afterEach(() => {
  wrapper.destroy();
});

describe("VideoPanel", () => {
  test("is a vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  test("props", () => {
    expect(wrapper.props()).toStrictEqual({
      vods: [],
      isModify: false,
    });
  });
});
